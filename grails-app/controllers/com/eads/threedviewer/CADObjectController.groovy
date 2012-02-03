package com.eads.threedviewer

import com.eads.threedviewer.co.CubeCO
import com.eads.threedviewer.co.CylinderCO
import com.eads.threedviewer.co.SphereCO
import grails.converters.JSON
import com.eads.threedviewer.co.ShapeCO
import grails.validation.ValidationException
import org.jcae.opencascade.jni.TopoDS_Shape
import org.jcae.opencascade.jni.BRepTools
import org.jcae.opencascade.jni.BRep_Builder
import com.eads.threedviewer.util.ShapeUtil
import com.eads.threedviewer.co.FileShapeCO

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
}