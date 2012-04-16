package com.eads.threedviewer

import com.eads.threedviewer.co.ShapeCO

class ProjectService {

    def cadObjectService

    CADObject addCADObject(ShapeCO co) {
        CADObject cadObject
        if (co.validate()) {
            cadObject = co.findOrCreateCADObject()
            addCADObject(co.project, cadObject)
            cadObject = saveCADObjectAndBrepFile(cadObject, co.file)
        } else {
            log.info co.errors
        }
        return cadObject
    }

    CADObject addCADObject(Project project, CADObject cadObject) {
        project.addToCadObjects(cadObject)
        project.save()
        return cadObject.save()
    }

    CADObject saveCADObjectAndBrepFile(CADObject cadObject, File file) {
        cadObject.save()
        cadObjectService.saveBrepFileOnFileSystem(cadObject, file)
        return cadObject
    }

}