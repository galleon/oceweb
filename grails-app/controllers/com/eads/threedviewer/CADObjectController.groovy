package com.eads.threedviewer

import com.eads.threedviewer.co.CubeCO
import com.eads.threedviewer.co.CylinderCO
import com.eads.threedviewer.co.SphereCO
import grails.converters.JSON

class CADObjectController {

    def createCube(CubeCO co) {
        render co.data as JSON
    }

    def createCylinder(CylinderCO co) {
        render co.data.data as JSON
    }

    def createSphere(SphereCO co) {
        render co.data.data as JSON
    }
}