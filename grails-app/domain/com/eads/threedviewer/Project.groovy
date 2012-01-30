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
    static belongsTo = [createdBy: User]

/* Constraints */
    static constraints = {
        name(unique: 'createdBy')
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

