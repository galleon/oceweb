package com.eads.threedviewer

import com.eads.threedviewer.co.ShapeCO

class ProjectService {

    CADObject addCADObject(ShapeCO co) {
        CADObject cadObject
        if (co.validate()) {
            cadObject = co.CADObject
            Project project = co.project
            cadObject.save()
            project.addToCadObjects(cadObject)
            project.save()
        }else{
            log.debug co.errors
        }
        return cadObject
    }

}
