package com.eads.threedviewer.co

import com.eads.threedviewer.CADMeshObject
import com.eads.threedviewer.CADObject
import com.eads.threedviewer.enums.ShapeType
import com.eads.threedviewer.util.ShapeUtil
import groovy.transform.ToString
import groovy.util.logging.Log
import org.codehaus.groovy.grails.validation.Validateable
import org.jcae.opencascade.jni.TopoDS_Shape
import com.eads.threedviewer.CADCubeObject

@Log
@Validateable
@ToString(includeNames = true, includeFields = true, excludes = 'metaClass,errors')
class MeshCO extends ShapeCO {
    CADObject parent
    float size
    float deflection

    static constraints = {
    }

    @Override
    TopoDS_Shape getShape() {
        return ShapeUtil.getShape(content)
    }

    CADObject findOrCreateCADObject() {
        CADObject cadObject = id ? CADMeshObject.get(id) : new CADMeshObject()
        cadObject.name = name ?: cadObject.name
        cadObject.x = x ?: cadObject.x
        cadObject.y = y ?: cadObject.y
        cadObject.z = z ?: cadObject.z
        cadObject.size = size ?: cadObject.size
        cadObject.deflection = deflection ?: cadObject.deflection
        cadObject.parent = parent ?: cadObject.parent
        cadObject.type = type ?: cadObject.type
        cadObject.project = project ?: cadObject.project
        return cadObject
    }
}