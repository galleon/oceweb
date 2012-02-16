package com.eads.threedviewer

import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString

@ToString(includeNames = true, includeFields = true, excludes = 'dateCreated,lastUpdated,metaClass')
@EqualsAndHashCode

class CADCylinderObject extends CADObject{
/* Dependency Injections (e.g. Services) */

/* Fields */
    double radius
    double height

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

