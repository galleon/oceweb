package com.eads.threedviewer

import com.eads.threedviewer.co.BooleanOperationCO
import com.eads.threedviewer.co.MeshCO
import com.eads.threedviewer.dto.ShapeDTO
import com.eads.threedviewer.enums.Operation
import com.eads.threedviewer.enums.ShapeType
import com.eads.threedviewer.util.ShapeUtil
import java.nio.channels.FileChannel
import org.jcae.mesh.amibe.algos1d.Compat1D2D
import org.jcae.mesh.amibe.algos1d.UniformLength
import org.jcae.mesh.amibe.algos1d.UniformLengthDeflection
import org.jcae.mesh.amibe.ds.MMesh1D
import org.jcae.mesh.amibe.ds.MeshParameters
import org.jcae.mesh.amibe.patch.InvalidFaceException
import org.jcae.mesh.amibe.patch.Mesh2D
import org.jcae.mesh.amibe.traits.MeshTraitsBuilder
import org.jcae.mesh.cad.CADExplorer
import org.jcae.mesh.cad.CADShape
import org.jcae.mesh.cad.CADShapeEnum
import org.jcae.mesh.cad.CADShapeFactory
import org.jcae.mesh.xmldata.MMesh1DWriter
import org.jcae.mesh.xmldata.MeshToMMesh3DConvert
import org.jcae.mesh.xmldata.MeshWriter
import org.jcae.mesh.amibe.algos2d.*
import org.jcae.opencascade.jni.*

class ShapeService {

    def projectService
    def fileService

    List<CADObject> saveSubCadObjects(CADObject cadObject, TopAbs_ShapeEnum shapeType) {
        List<CADObject> cadObjects = []
        if (cadObject) {
            explode(cadObject, shapeType).eachWithIndex {TopoDS_Shape shape, int index ->
                File file = ShapeUtil.getFile(shape, cadObject.name)
                CADObject currentObject = saveSubCadObjects(cadObject, "${cadObject.name}_${index}", file, shapeType)
                file.delete()
                if (currentObject) {
                    cadObjects.add(currentObject)
                }
            }
        }

        return cadObjects
    }

    List<TopoDS_Shape> explode(CADObject cadObject, TopAbs_ShapeEnum shapeType) {
        TopoDS_Shape shape = ShapeUtil.getShape(cadObject.findBrepFile())
        return explode(shape, shapeType)
    }

    List<TopoDS_Shape> explode(TopoDS_Shape shape, TopAbs_ShapeEnum shapeType) {
        TopExp_Explorer explorer = new TopExp_Explorer();
        int index = 1
        List<TopoDS_Shape> shapes = []

        for (explorer.init(shape, shapeType); explorer.more(); explorer.next()) {
            shapes.add(explorer.current())
            index++
        }
        return shapes
    }

    CADObject saveSubCadObjects(CADObject cadObject, String name, File file, TopAbs_ShapeEnum type) {
        CADObject subCadObject = new CADExplodeObject(name: name, project: cadObject.project, parent: cadObject, type: ShapeType.EXPLODE, explodeType: type)
        return projectService.saveCADObjectAndBrepFile(subCadObject, file)
    }

    CADObject saveCADObject(BooleanOperationCO co) {
        CADObject cadObject1 = co.CADObject1
        TopoDS_Shape shape1 = cadObject1.findShape()
        TopoDS_Shape shape2 = co.CADObject2.findShape()
        BRepAlgoAPI_BooleanOperation object = getOperationInstance(co, shape1, shape2)
        return saveCADObject(co, object.shape(), cadObject1.project)
    }

    CADObject saveCADObject(BooleanOperationCO co, TopoDS_Shape shape, Project project) {
        File file = ShapeUtil.getFile(shape, co.operation)
        CADObject cadObject = saveCADObject(project, co.name, file)
        file.delete()
        return cadObject
    }

    CADObject saveCADObject(Project project, String name, File file) {
        CADObject cadObject = new CADObject(name: name, project: project, type: ShapeType.COMPOUND)
        return projectService.saveCADObjectAndBrepFile(cadObject, file)
    }

    private BRepAlgoAPI_BooleanOperation getOperationInstance(BooleanOperationCO co, TopoDS_Shape shape1, TopoDS_Shape shape2) {
        def object
        if (co.operation == Operation.FUSE.toString()) {
            object = new BRepAlgoAPI_Fuse(shape1, shape2);
        }
        else if (co.operation == Operation.COMMON.toString()) {
            object = new BRepAlgoAPI_Common(shape1, shape2);
        }
        else if (co.operation == Operation.CUT.toString()) {
            object = new BRepAlgoAPI_Cut(shape1, shape2)
        }
        return object
    }

    CADMeshObject saveMesh(MeshCO co) {
        CADMeshObject cadObject
        cadObject = co.findOrCreateCADObject() as CADMeshObject
        cadObject = projectService.addCADObject(co.project, cadObject as CADObject)
        if (cadObject) {
            File file = generateMeshFolder(co, cadObject.id)
            log.info "CadMeshObject saved successfuly. Moving ${file.path} to ${cadObject.filesFolderPath}"
            fileService.renameFolder(file, cadObject.filesFolderPath)
            saveSubMeshes(cadObject)
        }
        else {
            logErrors(cadObject)
        }
        return cadObject
    }

    CADMeshObject updateMesh(MeshCO co) {
        CADMeshObject cadObject = co.findOrCreateCADObject() as CADMeshObject
        cadObject.save()
    }

    void logErrors(CADObject cadObject) {
        log.info "Error while saving ${cadObject} -:"
        cadObject.errors.allErrors.each {
            log.info "${it}"
        }
    }

    List<CADMeshObject> saveSubMeshes(CADMeshObject cadMeshObject) {
        List<CADMeshObject> cadMeshObjects = []
        List<ShapeDTO> shapeDTOs = ShapeDTO.getUnvGroups(cadMeshObject.unvFilePath)
        shapeDTOs.each {ShapeDTO shapeDTO ->
            log.info "creating mesh sub object for entitycount ${shapeDTO.entitiesCount}"
            CADMeshObject subCadMeshObject = saveSubMesh(cadMeshObject, shapeDTO)
            if (subCadMeshObject) {
                cadMeshObjects.add(subCadMeshObject)
            }
        }
        return cadMeshObjects
    }

    CADMeshObject saveSubMesh(CADMeshObject cadMeshObject, ShapeDTO shapeDTO) {
        CADMeshObject subCadMeshObject = createSubCADMesh(cadMeshObject, shapeDTO)
        projectService.saveCADObjectAndUnvFile(subCadMeshObject, shapeDTO.createUnvFile())
        return subCadMeshObject
    }

    CADMeshObject createSubCADMesh(CADMeshObject cadMeshObject, ShapeDTO shapeDTO) {
        CADMeshObject subCadMeshObject = new CADMeshObject(parent: cadMeshObject, project: cadMeshObject.project, name: "${cadMeshObject.name}_${shapeDTO.groupName}",
                size: cadMeshObject.size, deflection: cadMeshObject.deflection, type: cadMeshObject.type, groupName: shapeDTO.groupName)
        return subCadMeshObject
    }

    File merge(List<ShapeDTO> shapeDTOs) {

    }

    //TODO -: Refactore code and check why its not working in co class so that project service method of creating cadobject can be used
    File generateMeshFolder(MeshCO co, Long id) {
        float size = co.size
        float deflection = co.deflection
        File file = co.findOrCreateCADObject().parent.findBrepFile()
        String brepfile = file.name
        String outputDir = "/tmp/${id}"
        String brepdir = file.parent

        if (brepfile.indexOf((int) File.separatorChar) >= 0) {
            int idx = brepfile.lastIndexOf((int) File.separatorChar)
            brepdir = brepfile.substring(0, idx)
            brepfile = brepfile.substring(idx + 1)
        }

        String unvName = "${outputDir}/${id}.unv"
        log.info "Output Dir -: ${outputDir} ,unvName -: ${unvName} ,brepDir -: ${brepdir}"

        File xmlDirF = new File(outputDir);
        xmlDirF.mkdirs();
        if (!xmlDirF.exists() || !xmlDirF.isDirectory()) {
            log.info "Cannot write to ${outputDir}"
        } else {
            CADShapeFactory factory = CADShapeFactory.getFactory()

            if (!brepdir.equals(outputDir)) {
                FileInputStream is = null;
                FileOutputStream os = null;
                try {
                    is = new FileInputStream(brepdir + File.separator + brepfile);
                    FileChannel iChannel = is.getChannel();
                    os = new FileOutputStream(new File(outputDir, brepfile), false);
                    FileChannel oChannel = os.getChannel();
                    oChannel.transferFrom(iChannel, 0, iChannel.size());
                } finally {
                    if (is != null) is.close();
                    if (os != null) os.close();
                }
            }

            MMesh1D mesh1d = new MMesh1D(outputDir + File.separator + brepfile)
            CADShape shape = mesh1d.getGeometry();
            HashMap<String, String> options1d = new HashMap<String, String>()
            options1d.put("size", "" + size)

            if (deflection <= 0.0) {
                new UniformLength(mesh1d, options1d).compute()
            } else {
                options1d.put("deflection", "" + deflection)
                options1d.put("relativeDeflection", "true")
                new UniformLengthDeflection(mesh1d, options1d).compute()
                new Compat1D2D(mesh1d, options1d).compute()
            }

            MMesh1DWriter.writeObject(mesh1d, outputDir, brepfile)
            log.info "Edges discretized"

// Mesh 2D
            mesh1d.duplicateEdges()
            mesh1d.updateNodeLabels()

            HashMap<String, String> options2d = new HashMap<String, String>()
            options2d.put("size", "" + size)
            options2d.put("deflection", "" + deflection)
            options2d.put("relativeDeflection", "true")
            options2d.put("isotropic", "true")

            HashMap<String, String> smoothOptions2d = new HashMap<String, String>()
            smoothOptions2d.put("modifiedLaplacian", "true")
            smoothOptions2d.put("refresh", "false")
            smoothOptions2d.put("iterations", "5")
            smoothOptions2d.put("tolerance", "1")
            smoothOptions2d.put("relaxation", "0.6")

            MeshTraitsBuilder mtb = MeshTraitsBuilder.getDefault2D()
            CADExplorer expl = factory.newExplorer()
            List seen = []
            List bads = []
            int iface = 0
            CADShape face

            for (expl.init(shape, CADShapeEnum.FACE); expl.more(); expl.next()) {
                face = expl.current()
                iface++
                if (!(face in seen)) {
                    seen << face

                    MeshParameters mp = new MeshParameters(options2d)
                    Mesh2D mesh = new Mesh2D(mtb, mp, face)

                    boolean success = true
                    try {
                        new Initial(mesh, mtb, mesh1d).compute()
                    } catch (InvalidFaceException ex) {
                        log.info "Face #${iface} is invalid. Skipping ..."
                        success = false
                    } catch (Exception ex) {
                        ex.printStackTrace()
                        log.info "Unexpected error when triangulating face #${iface}. Skipping ..."
                        success = false
                    }
                    if (!success) {
                        bads << iface
                        BRepTools.write(face.getShape(), "error.brep")
                        log.info "Bogus face has been written into error.brep file"
                        mesh = new Mesh2D(mtb, mp, face)
                    } else {
                        new BasicMesh(mesh).compute()
                        new SmoothNodes2D(mesh, smoothOptions2d).compute()
                        new ConstraintNormal3D(mesh).compute()
                        new CheckDelaunay(mesh).compute()

                        log.info "Face #${iface} has been meshed"
                    }
                    MeshWriter.writeObject(mesh, outputDir, brepfile, iface)
                }
            }

// Mesh 3D
            expl = factory.newExplorer()
            MeshToMMesh3DConvert m2dto3d = new MeshToMMesh3DConvert(outputDir, brepfile, shape)
            m2dto3d.exportUNV(unvName != null, unvName)

            iface = 0
            for (expl.init(shape, CADShapeEnum.FACE); expl.more(); expl.next()) {
                iface++
            }
            int[] iArray = new int[iface]
            for (int i = 0; i < iface; i++) iArray[i] = i + 1
            m2dto3d.collectBoundaryNodes(iArray)
            m2dto3d.beforeProcessingAllShapes(false)
            iface = 0

            for (expl.init(shape, CADShapeEnum.FACE); expl.more(); expl.next()) {
                face = expl.current()
                iface++
                m2dto3d.processOneShape(iface, "" + iface, iface)
            }
            m2dto3d.afterProcessingAllShapes()
        }
        return xmlDirF
    }
}