package com.eads.threedviewer

import com.eads.threedviewer.co.ShapeCO
import com.eads.threedviewer.dto.ShapeDTO

class ProjectService {

    CADObject addCADObject(ShapeCO co) {
        CADObject cadObject
        if (co.validate()) {
            cadObject = co.findOrCreateCADObject()
            cadObject.content = co.content
            Project project = co.project
            ShapeDTO shapeDTO = co.shapeDTO
            cadObject = saveCADObject(cadObject, shapeDTO)
            project.addToCadObjects(cadObject)
            project.save()

        } else {
            log.info co.errors
        }
        return cadObject
    }

    CADObject saveCADObject(CADObject cadObject, ShapeDTO shapeDTO) {
        cadObject.vertices = shapeDTO.vertices.join(",")
        cadObject.edges = shapeDTO.edges.join(",")
        cadObject.faces = shapeDTO.faces.join(",")
        cadObject.save()
    }

}
