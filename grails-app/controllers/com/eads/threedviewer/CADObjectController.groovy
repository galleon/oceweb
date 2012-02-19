package com.eads.threedviewer

import org.jcae.opencascade.jni.TopAbs_ShapeEnum
import org.jcae.opencascade.jni.TopoDS_Shape
import org.jcae.mesh.cad.CADShapeFactory
import org.jcae.mesh.amibe.ds.MMesh1D
import org.jcae.mesh.cad.CADShape
import org.jcae.mesh.amibe.algos1d.UniformLength
import org.jcae.mesh.amibe.algos1d.UniformLengthDeflection
import org.jcae.mesh.amibe.algos1d.Compat1D2D
import org.jcae.mesh.xmldata.MMesh1DWriter
import org.jcae.mesh.amibe.traits.MeshTraitsBuilder
import org.jcae.mesh.cad.CADExplorer
import org.jcae.mesh.cad.CADShapeEnum
import org.jcae.mesh.amibe.ds.MeshParameters
import org.jcae.mesh.amibe.patch.Mesh2D
import org.jcae.mesh.amibe.algos2d.Initial
import org.jcae.mesh.amibe.patch.InvalidFaceException
import org.jcae.opencascade.jni.BRepTools
import org.jcae.mesh.amibe.algos2d.BasicMesh
import org.jcae.mesh.amibe.algos2d.SmoothNodes2D
import org.jcae.mesh.amibe.algos2d.ConstraintNormal3D
import org.jcae.mesh.amibe.algos2d.CheckDelaunay
import org.jcae.mesh.xmldata.MeshWriter
import org.jcae.mesh.xmldata.MeshToMMesh3DConvert
import com.eads.threedviewer.enums.ShapeType
import com.eads.threedviewer.util.ShapeUtil
import grails.converters.JSON
import grails.validation.ValidationException
import java.nio.channels.FileChannel
import com.eads.threedviewer.co.*

class CADObjectController {

    def projectService
    def shapeService

    def createCube(CubeCO co) {
        sendResponse(co)
    }

    def createCylinder(CylinderCO co) {
        sendResponse(co)
    }

    def createCone(ConeCO co) {
        sendResponse(co)
    }

    def createSphere(SphereCO co) {
        sendResponse(co)
    }

    def createShapeFromFile(FileShapeCO co) {
        sendResponse(co)
    }

    def createMesh(MeshCO co) {
        byte[] content = getContent(co)
        CADObject cadObject = co.CADObject
        cadObject.content = content
        try {
            cadObject.save()
        } catch (ValidationException ve) {
            flash.error = ve.message
            redirect(controller: 'project', action: 'index', params: [shapeId: co.parent.id])
            return false
        }
        redirect(controller: 'project', action: 'index', params: [shapeId: cadObject.id])
    }

    def editCube(CubeCO co) {
        editResponse(co)
    }

    def editCylinder(CubeCO co) {
        editResponse(co)
    }

    def editCone(CubeCO co) {
        editResponse(co)
    }

    def editSphere(CubeCO co) {
        editResponse(co)
    }

    Closure editResponse = {ShapeCO co ->
        CADObject cadObject
        try {
            cadObject = projectService.editCADObject(co)
        }
        catch (ValidationException ve) {
            flash.error = ve.message
        }
        if (cadObject) {
            redirect(controller: 'project', action: 'index', params: [shapeId: cadObject.id])
        } else {
            render(view: '/project/index', model: [project: co.project, projects: Project.list(), co: co])
        }
    }

    Closure sendResponse = {ShapeCO co ->
        CADObject cadObject
        try {
            cadObject = projectService.addCADObject(co)
        } catch (ValidationException ve) {
            flash.error = ve.message
        }
        if (cadObject) {
            redirect(controller: 'project', action: 'index', params: [shapeId: cadObject.id])
        } else {
            render(view: '/project/index', model: [project: co.project, projects: Project.list(), co: co])
        }
    }

    def show(Long id) {
        Map result
        CADObject cadObject = id ? CADObject.get(id) : null
        if (cadObject) {
            File file = cadObject.createFile()
            try {
                result = ShapeUtil.getData(file)
            } catch (RuntimeException rte) {
                result = ['error': rte.message]
            }
            file.delete()
        } else {
            result = ['error': "Object not found for id ${id}"]
        }
        render result as JSON

    }

    def explode(Long id) {
        CADObject cadObject = id ? CADObject.get(id) : null
        TopAbs_ShapeEnum shapeType = params.shape as TopAbs_ShapeEnum
        shapeService.saveSubCadObjects(cadObject, shapeType)
        redirect(controller: 'project', action: 'index', params: [name: cadObject?.project?.name])
    }

    def booleanOperation(BooleanOperationCO co) {
        CADObject cadObject = shapeService.createBooleanObject(co)
        redirect(controller: 'project', action: 'index', params: [name: cadObject?.project?.name, shapeId: cadObject.id])
    }

    def edit() {
        CADObject cadObject = CADObject.get(params.id)
        if (cadObject.type == ShapeType.CUBE) {
            render(template: '/cadObject/editCube', model: [cadObject: cadObject, project: cadObject.project])
        }
        if (cadObject.type == ShapeType.CONE) {
            render(template: '/cadObject/editCone', model: [cadObject: cadObject, project: cadObject.project])
        }
        if (cadObject.type == ShapeType.CYLINDER) {
            render(template: '/cadObject/editCylinder', model: [cadObject: cadObject, project: cadObject.project])
        }
        if (cadObject.type == ShapeType.SPHERE) {
            render(template: '/cadObject/editSphere', model: [cadObject: cadObject, project: cadObject.project])
        }
    }

    def editShape(Long id) {
    }


    def delete() {
        Map result = ['success': 'Deleted Successfully']
        Set<Long> ids = params.list('ids')
        List<CADObject> cadObjects = ids ? CADObject.getAll(ids.toList()) : []
        try {
            CADObject.findAllByParentInList(cadObjects)*.delete()
            cadObjects*.delete()
        } catch (RuntimeException rte) {
            result = ['error': message(code: "error.occured.while.serving.your.request")]
        }

        render result as JSON
    }

    def updateName(Long id, String name) {
        CADObject cadObject = id ? CADObject.get(id) : null
        if (cadObject) {
            cadObject.name = name
        }
        render "success"
    }

    //TODO -: Refactore code and check why its not working in co classed so that project service method of creating cadobject can be used
    private byte[] getContent(MeshCO co) {
        Long id = co.parent.id
        float size = co.size
        float deflection = co.deflection
        File file = co.parent.createFile()
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
            System.exit(1);
        }

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
        file.delete()
        byte[] unvContent = new File(unvName).bytes
//        xmlDirF.deleteDir()
        return unvContent
    }
}