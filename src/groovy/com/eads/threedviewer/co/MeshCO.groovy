package com.eads.threedviewer.co

import com.eads.threedviewer.CADObject
import groovy.transform.ToString
import org.codehaus.groovy.grails.validation.Validateable
import org.jcae.opencascade.jni.TopoDS_Shape
import com.eads.threedviewer.util.ShapeUtil
import com.eads.threedviewer.CADMeshObject
import com.eads.threedviewer.enums.ShapeType

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

    CADObject getCADObject() {
        return new CADMeshObject(name: name, x: 0, y: 0, z: 0, type: ShapeType.MESH, project: project, size: size, deflection: deflection, parent: parent)
    }
}