package com.eads.threedviewer.co

import com.eads.threedviewer.CADObject
import com.eads.threedviewer.util.ShapeUtil
import grails.validation.Validateable
import org.jcae.opencascade.jni.TopoDS_Shape

@Validateable
class ExplodeShapeCO extends ShapeCO{

    TopoDS_Shape getShape() {
        return ShapeUtil.getShape(content)
    }

    byte[] getContent() {
        CADObject cadObject = super.findOrCreateCADObject()
        return cadObject?.readBytes()
    }

}
