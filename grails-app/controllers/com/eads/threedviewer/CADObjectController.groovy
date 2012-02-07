package com.eads.threedviewer

import com.eads.threedviewer.util.ShapeUtil
import grails.converters.JSON
import grails.validation.ValidationException
import com.eads.threedviewer.co.*

class CADObjectController {

    def projectService

    def createCube(CubeCO co) {
        sendResponse(co)
    }

    def createCylinder(CylinderCO co) {
        sendResponse(co)
    }

    def createSphere(SphereCO co) {
        sendResponse(co)
    }

    def createShapeFromFile(FileShapeCO co) {
        CADObject cadObject
        try {
            cadObject = projectService.addCADObject(co)
        } catch (ValidationException ve) {
            log.error ve.message
        }
        if (cadObject) {
            render(view: '/project/index', model: [project: cadObject.project, projects: Project.list(), shapeId: cadObject.id])
        } else {
            render(view: '/project/index', model: [project: co.project, projects: Project.list(), co: co])
        }
    }

    Closure sendResponse = {ShapeCO co ->
        Map result
        try {
            CADObject cadObject = projectService.addCADObject(co)
            if (cadObject) {
                result = co.data
            } else {
                result = ['error': message(code: "error.occured.while.serving.your.request")]
            }
        } catch (ValidationException ve) {
            result = ['error': message(code: "error.occured.while.serving.your.request")]
        }
        render result as JSON
    }

    def show(Long id) {
        Map result
        CADObject cadObject = id ? CADObject.get(id) : null
        if (cadObject) {
            result = ShapeUtil.getData(cadObject.file)
        } else {
            result = ['error': "Object not found for id ${id}"]
        }
        render result as JSON

    }

    def delete() {
        Map result = ['success': 'Deleted Successfully']
        List<Long> ids = params.list('ids')
        List<CADObject> cadObjects = ids ? CADObject.getAll(ids) : []
        try {
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