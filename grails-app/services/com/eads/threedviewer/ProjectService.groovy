package com.eads.threedviewer

import com.eads.threedviewer.co.ShapeCO
import com.eads.threedviewer.dto.ShapeDTO

class ProjectService {

    def cadObjectService

    CADObject addCADObject(ShapeCO co) {
        CADObject cadObject
        if (co.validate()) {
            cadObject = co.findOrCreateCADObject()
            Project project = co.project
            cadObject = saveCADObject(cadObject, co.file)
            project.addToCadObjects(cadObject)
            project.save()

        } else {
            log.info co.errors
        }
        return cadObject
    }

    CADObject saveCADObject(CADObject cadObject, File file) {
        cadObject.save()
        cadObjectService.saveBrepFileOnFileSystem(cadObject, file)
        return cadObject
    }

}