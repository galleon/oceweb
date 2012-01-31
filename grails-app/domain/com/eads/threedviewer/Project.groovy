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

/* Relations */
    static hasMany = [cadObjects: CADObject]
/* Constraints */
    static constraints = {
        name(blank: false)
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

