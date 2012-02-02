package com.eads.threedviewer

import com.eads.threedviewer.co.ShapeCO

class ProjectService {

    CADObject addCADObject(ShapeCO co) {
        CADObject cadObject
        cadObject = co.CADObject
        Project project = co.project
        project.addToCadObjects(cadObject)
        project.save()
        return cadObject
    }
}
