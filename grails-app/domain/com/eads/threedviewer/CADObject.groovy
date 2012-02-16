package com.eads.threedviewer

import com.eads.threedviewer.util.ShapeUtil
import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString
import org.jcae.opencascade.jni.TopoDS_Shape
import com.eads.threedviewer.enums.ShapeType

@ToString(includeNames = true, includeFields = true, excludes = 'dateCreated,lastUpdated,metaClass,content')
@EqualsAndHashCode
class CADObject {
/* Dependency Injections (e.g. Services) */

/* Fields */
    String name
    double x
    double y
    double z
    CADObject parent
    byte[] content
    Date dateCreated
    Date lastUpdated
    ShapeType type

/* Transients */
    static transients = ['subCadObjects', 'shape']

/* Relations */
    static belongsTo = [project: Project, parent: CADObject]

/* Constraints */
    static constraints = {
        name(blank: false)
        content(maxSize: 1200000000)
        x(nullable: true)
        y(nullable: true)
        z(nullable: true)
        content(nullable: true)
        parent(nullable: true)
    }

/* Mappings */
    static mapping = {
    }

/* Hooks */

/* Named queries */

/* Transient Methods */

    List<CADObject> getSubCadObjects() {
        return CADObject.findAllByParent(this)
    }

    TopoDS_Shape getShape() {
        return ShapeUtil.getShape(this.content)
    }

/* Methods */

    File createFile() {
        return ShapeUtil.createBrepFile(content, "${project.name}_${name}_")
    }
/* Static Methods */
}

