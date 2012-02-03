package com.eads.threedviewer

import com.eads.threedviewer.co.ShapeCO

class ProjectService {

    CADObject addCADObject(ShapeCO co) {
        CADObject cadObject
        if (co.validate()) {
            cadObject = co.CADObject
            Project project = co.project
            project.addToCadObjects(cadObject)
            project.save()
        }else{
            println co.errors
        }
        return cadObject
    }
}
