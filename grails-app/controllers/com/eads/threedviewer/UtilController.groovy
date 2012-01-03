package com.eads.threedviewer

import org.jcae.opencascade.jni.BRepPrimAPI_MakeBox
import org.jcae.opencascade.jni.BRepTools

class UtilController {

    def index() {
        double[] p1 = [5, 5, 5]
        double[] p2 = [-5, -5, -5]

        BRepPrimAPI_MakeBox box = new BRepPrimAPI_MakeBox(p1, p2)
        BRepTools.write(box.shape(), "difference.brep")
        render "success"
    }
}
