package com.eads.threedviewer

import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString

@ToString(includeNames = true, includeFields = true, excludes = 'dateCreated,lastUpdated,metaClass,content')
@EqualsAndHashCode
class CADObject {
/* Dependency Injections (e.g. Services) */

/* Fields */
    String name
    byte[] content
    Date dateCreated
    Date lastUpdated

/* Transients */
    static transients = ['file']

/* Relations */
    static belongsTo = [project: Project]

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

    File getFile() {
        File file = File.createTempFile(project.name, ".brep")
        file.bytes = content
        return file
    }

/* Methods */

/* Static Methods */
}
