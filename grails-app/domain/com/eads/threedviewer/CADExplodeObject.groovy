package com.eads.threedviewer

import org.jcae.opencascade.jni.TopAbs_ShapeEnum
import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString

@ToString(includeNames = true, includeFields = true, excludes = 'dateCreated,lastUpdated,metaClass,content')
@EqualsAndHashCode
class CADExplodeObject extends CADObject {
/* Dependency Injections (e.g. Services) */

/* Fields */
    TopAbs_ShapeEnum explodeType
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
