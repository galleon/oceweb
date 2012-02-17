package com.eads.threedviewer

import com.eads.threedviewer.enums.ShapeType
import com.eads.threedviewer.util.ShapeUtil
import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString
import org.jcae.opencascade.jni.TopoDS_Shape

@ToString(includeNames = true, includeFields = true, excludes = 'dateCreated,lastUpdated,metaClass,content')
@EqualsAndHashCode
class CADMeshObject extends CADObject {
/* Dependency Injections (e.g. Services) */

/* Fields */
    float size
    float deflection
/* Transients */

/* Relations */

/* Constraints */

/* Mappings */
    static mapping = {
    }

/* Hooks */

/* Named queries */

/* Transient Methods */

/* Methods */

/* Static Methods */
}

