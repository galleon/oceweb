package com.eads.threedviewer

import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString

@ToString(includeNames = true, includeFields = true, excludes = 'dateCreated,lastUpdated,metaClass')
@EqualsAndHashCode

class Project {
/* Dependency Injections (e.g. Services) */

/* Fields */
    String name
    Date dateCreated
    Date lastUpdated

/* Transients */
    static transients = ['parentCadObjects']
/* Relations */
    static hasMany = [cadObjects: CADObject]
/* Constraints */
    static constraints = {
        name(blank: false)
    }

/* Mappings */
    static mapping = {
        cadObjects cascade: 'all-delete-orphan'
    }

/* Hooks */

/* Named queries */

/* Transient Methods */

    List<CADObject> getParentCadObjects() {
        return CADObject.findAllByProjectAndParentIsNull(this)
    }
/* Methods */

/* Static Methods */
}

