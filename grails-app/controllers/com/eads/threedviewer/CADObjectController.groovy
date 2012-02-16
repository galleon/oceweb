package com.eads.threedviewer

import com.eads.threedviewer.util.ShapeUtil
import grails.converters.JSON
import grails.validation.ValidationException
import com.eads.threedviewer.co.*
import org.jcae.opencascade.jni.TopAbs_ShapeEnum
import com.eads.threedviewer.enums.ShapeType

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
            result = ShapeUtil.getData(file)
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

    def edit(Long id){
        CADObject cadObject = CADObject.get(id)
        Map result  = ShapeService.getShapeInfo(cadObject)
        render result as JSON
    }

    def editShape(Long id){
        log.info(">>>>>>>>>>>>>>>>> params : "+params)
//        render ">>>>>>>>>>>>>>>>>>>>>>>>"+id.toString()
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
}