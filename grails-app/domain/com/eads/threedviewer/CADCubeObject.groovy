package com.eads.threedviewer

import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString

@ToString(includeNames = true, includeFields = true, excludes = 'dateCreated,lastUpdated,metaClass')
@EqualsAndHashCode
class CADCubeObject extends CADObject{
/* Dependency Injections (e.g. Services) */

/* Fields */
    double x1
    double y1
    double z1

/* Transients */

/* Relations */

/* Constraints */
    static constraints = {
    }

/* Mappings */
    static mapping = {
    }

/* Hooks */

/* Named queries */

/* Transient Methods */

/* Methods */

/* Static Methods */
}

