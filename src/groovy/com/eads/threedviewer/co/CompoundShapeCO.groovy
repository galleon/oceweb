package com.eads.threedviewer.co

import org.jcae.opencascade.jni.TopoDS_Shape
import com.eads.threedviewer.util.ShapeUtil
import com.eads.threedviewer.CADObject
import grails.validation.Validateable

@Validateable
class CompoundShapeCO extends ShapeCO {

    TopoDS_Shape getShape() {
        return ShapeUtil.getShape(content)
    }

    byte[] getContent() {
        CADObject cadObject = super.findOrCreateCADObject()
        return cadObject?.readBytes()
    }

}
