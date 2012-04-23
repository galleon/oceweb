package com.eads.threedviewer

import grails.converters.JSON
import grails.validation.ValidationException
import org.jcae.opencascade.jni.TopAbs_ShapeEnum
import com.eads.threedviewer.co.*

class CADObjectController {

    def cadObjectService
    def shapeService

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

    private Closure sendResponse = {ShapeCO co ->
        Map result
        CADObject cadObject
        try {
            cadObject = cadObjectService.save(co)
        } catch (RuntimeException rte) {
            rte.printStackTrace()
            result = ['error': rte.message]
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

    def saveMesh(MeshCO co) {
        Map result
        if (co.validate()) {
            try {
                CADMeshObject cadMeshObject = co.id ? cadObjectService.updateMesh(co) : cadObjectService.saveMesh(co)
                result = ['id': cadMeshObject?.id]
            } catch (RuntimeException rte) {
                result = ['error': rte.message]
            }
        } else {
            result = ['error': co.errors]
        }
        render result as JSON

    }

    def show(Long id) {
        Map result
        CADObject cadObject = id ? CADObject.get(id) : null
        if (cadObject) {
            try {
                result = cadObject.readData()
            } catch (RuntimeException rte) {
                result = ['error': rte.message]
            }
        } else {
            result = ['error': "Object not found for id ${id}"]
        }
        render result as JSON

    }

    def explode(Long id) {
        Map result = ['success': 'Shape exploded successfully']
        CADObject cadObject = id ? CADObject.get(id) : null
        TopAbs_ShapeEnum shapeType = params.shape as TopAbs_ShapeEnum
        try {
            cadObjectService.saveSubCadObjects(cadObject, shapeType)
        } catch (Exception e) {
            result = ['error': e.message]
        }
        render result as JSON
    }

    def booleanOperation(BooleanOperationCO co) {
        CADObject cadObject
        Map result
        try {
            cadObject = cadObjectService.saveCADObject(co)
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

    JSON delete() {
        Set<Long> ids = params.list('ids')
        Map result = ['success': 'Deleted Successfully']
        try {
            cadObjectService.delete(ids)
        } catch (RuntimeException rte) {
            result = ['error': "Error Occured while serving your request"]
        }
        render result as JSON
    }

    def runSimulation(SimulationCO co) {
        CADMeshObject cadMeshObject = co.id ? CADMeshObject.get(co.id) : null
        Map result = ["success": "Simulation completed"]
        if (cadMeshObject) {
            File file = shapeService.runSimulation(cadMeshObject.findUnvFile(), co)
        } else {
            result = ['error': "Object not found for id ${co.id}"]
        }
        result as JSON
    }

    def merge() {
        Map result = ['success': 'Deleted Successfully']
        Set ids = params.list('ids')*.toLong()
        List<CADMeshObject> cadMeshObjects = ids ? CADMeshObject.findAllByIdInList(ids.toList()) : []
        try {
            cadObjectService.merge(cadMeshObjects)
        } catch (RuntimeException rte) {
            result = ['error': rte.message]
        }
        render result as JSON
    }

    def rename(Long id, String name) {
        CADObject cadObject = id ? CADObject.get(id) : null
        Map result = [:]
        if (cadObject) {
            cadObject.name = name
            try {
                cadObject.save()
                result['success'] = "Name updated successfuly"
            } catch (RuntimeException rte) {
                result['error'] = rte.message
            }
        } else {
            result['error'] = "Object not found for id ${id}"
        }
        render result as JSON
    }
}