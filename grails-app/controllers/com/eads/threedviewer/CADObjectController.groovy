package com.eads.threedviewer

import com.eads.threedviewer.dto.ShapeDTO
import grails.converters.JSON
import grails.validation.ValidationException
import org.jcae.opencascade.jni.TopAbs_ShapeEnum
import com.eads.threedviewer.co.*

class CADObjectController {

    def projectService
    def shapeService
    def cadObjectService

    def saveCube(CubeCO co) {
        sendResponse(co)
    }

    def saveCylinder(CylinderCO co) {
        sendResponse(co)
    }

    def saveCone(ConeCO co) {
        sendResponse(co)
    }

    def saveSphere(SphereCO co) {
        sendResponse(co)
    }

    def saveShapeFromFile(FileShapeCO co) {
        sendResponse(co)
    }

    def saveCompound(CompoundShapeCO co) {
        sendResponse(co)
    }

    def saveExplode(ExplodeShapeCO co) {
        sendResponse(co)
    }

    def saveMesh(MeshCO co) {
        Map result
        if (co.validate()) {
            try {
                CADMeshObject cadMeshObject = co.id ? shapeService.updateMesh(co) : shapeService.saveMesh(co)
                result = ['id': cadMeshObject?.id]
            } catch (ValidationException ve) {
                result = ['error': ve.message]
            }
        } else {
            result = ['error': co.errors]
        }
        render result as JSON

    }

    Closure sendResponse = {ShapeCO co ->
        Map result
        CADObject cadObject
        try {
            cadObject = projectService.addCADObject(co)
        } catch (ValidationException ve) {
            result = ['error': ve.message]
        }
        if (cadObject) {
            result = ['id': cadObject?.id]
        } else {
            result = result ?: ['error': cadObject.errors]
        }
        render result as JSON
    }

    def show(Long id) {
        Map result
        CADObject cadObject = id ? CADObject.get(id) : null
        if (cadObject) {
            result = cadObject.data
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
        CADObject cadObject
        Map result
        try {
            cadObject = shapeService.saveCADObject(co)
        } catch (ValidationException ve) {
            result = ['error': ve.message]
        }
        if (cadObject) {
            result = ['id': cadObject?.id]
        } else {
            result = result ?: ['error': cadObject.errors]
        }
        render result as JSON
    }

    def createCube(CubeCO co) {
        renderTemplate(co)
    }

    def createCylinder(CylinderCO co) {
        renderTemplate(co)
    }

    def createCone(ConeCO co) {
        renderTemplate(co)
    }

    def createSphere(SphereCO co) {
        renderTemplate(co)
    }

    def createFile(FileShapeCO co) {
        renderTemplate(co)
    }

    def createCompound(CompoundShapeCO co) {
        renderTemplate(co)
    }

    def createExplode(ExplodeShapeCO co) {
        renderTemplate(co)
    }

    def createMesh(MeshCO co) {
        renderTemplate(co)
    }

    Closure renderTemplate = {ShapeCO shapeCO ->
        CADObject cadObject = shapeCO.findOrCreateCADObject()
        render(template: "/cadObject/${cadObject.type.toString().toLowerCase()}Info", model: [cadObject: cadObject])
    }

    JSON delete() {
        Set<Long> ids = params.list('ids')
        Map result = ['success': 'Deleted Successfully']
        try {
            cadObjectService.deleteCADObject(ids)
        } catch (RuntimeException rte) {
            result = ['error': message(code: "error.occured.while.serving.your.request")]
        }
        render result as JSON
    }

}