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

    Closure sendResponse = {ShapeCO co ->
        Map result
        try {
            projectService.addCADObject(co)
            result = co.data
        } catch (ValidationException ve) {
            result = ['error': ve.message]
        }
        render result as JSON
    }

    def show(Long id) {
        Map result
        CADObject cadObject = id ? CADObject.get(id) : null
        if (cadObject) {
            File file = cadObject.file
            TopoDS_Shape shape = BRepTools.read(file.path, new BRep_Builder())
            result = ShapeUtil.getData(shape)
        } else {
            result = ['error': "Object not found for id ${id}"]
        }
        render result as JSON

    }
}