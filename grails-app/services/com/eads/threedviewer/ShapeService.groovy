package com.eads.threedviewer

import com.eads.threedviewer.co.BooleanOperationCO
import com.eads.threedviewer.co.MeshCO
import com.eads.threedviewer.co.SimulationCO
import com.eads.threedviewer.enums.Operation
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

    def fileService

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

    BRepAlgoAPI_BooleanOperation getOperationInstance(BooleanOperationCO co, TopoDS_Shape shape1, TopoDS_Shape shape2) {
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

    //TODO -: Implement this method
    File runSimulation(File unvFile, SimulationCO co) {
        log.info "Simulation started for ${co} ${unvFile.name}"
        return fileService.getDummySimulatedFile()
    }

    //TODO -: Refactore code and check why its not working in co class so that project service method of creating cadobject can be used
    File generateMeshFolder(MeshCO co, String folderPath, Long id) {
        float size = co.size
        float deflection = co.deflection
        File file = co.findOrCreateCADObject().parent.findBrepFile()
        String brepfile = file.name
        String brepdir = file.parent

        if (brepfile.indexOf((int) File.separatorChar) >= 0) {
            int idx = brepfile.lastIndexOf((int) File.separatorChar)
            brepdir = brepfile.substring(0, idx)
            brepfile = brepfile.substring(idx + 1)
        }

        String unvName = "${folderPath}/${id}.unv"
        log.info "Output Dir -: ${folderPath} ,unvName -: ${unvName} ,brepDir -: ${brepdir}"

        File xmlDirF = new File(folderPath);
        xmlDirF.mkdirs();
        if (!xmlDirF.exists() || !xmlDirF.isDirectory()) {
            log.info "Cannot write to ${folderPath}"
        } else {
            CADShapeFactory factory = CADShapeFactory.getFactory()

            if (!brepdir.equals(folderPath)) {
                FileInputStream is = null;
                FileOutputStream os = null;
                try {
                    is = new FileInputStream(brepdir + File.separator + brepfile);
                    FileChannel iChannel = is.getChannel();
                    os = new FileOutputStream(new File(folderPath, brepfile), false);
                    FileChannel oChannel = os.getChannel();
                    oChannel.transferFrom(iChannel, 0, iChannel.size());
                } finally {
                    if (is != null) is.close();
                    if (os != null) os.close();
                }
            }

            MMesh1D mesh1d = new MMesh1D(folderPath + File.separator + brepfile)
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

            MMesh1DWriter.writeObject(mesh1d, folderPath, brepfile)
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
                    MeshWriter.writeObject(mesh, folderPath, brepfile, iface)
                }
            }

// Mesh 3D
            expl = factory.newExplorer()
            MeshToMMesh3DConvert m2dto3d = new MeshToMMesh3DConvert(folderPath, brepfile, shape)
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
        fileService.deleteFilesExcept(xmlDirF, new File(unvName))
        return xmlDirF
    }
}