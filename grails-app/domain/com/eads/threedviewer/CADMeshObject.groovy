package com.eads.threedviewer

import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString

@ToString(includeNames = true, includeFields = true, excludes = 'dateCreated,lastUpdated,metaClass,content')
@EqualsAndHashCode
class CADMeshObject extends CADObject {
/* Dependency Injections (e.g. Services) */

/* Fields */
    Integer groupName
    float size
    float deflection
/* Transients */
/* Relations */

/* Constraints */
    static constraints = {
        groupName(nullable: false)
        size(nullable: false)
        deflection(nullable: false)
    }

/* Mappings */
    static mapping = {
    }

/* Hooks */

/* Named queries */

/* Transient Methods */

    Map getData() {
        Map result = super.data
        result['wireframe'] = true
        return result
    }

/* Methods */

/* Static Methods */
}

