package com.eads.threedviewer

import com.eads.threedviewer.util.ShapeUtil
import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString

@ToString(includeNames = true, includeFields = true, excludes = 'dateCreated,lastUpdated,metaClass,content')
@EqualsAndHashCode
class CADObject {
/* Dependency Injections (e.g. Services) */

/* Fields */
    String name
    double x
    double y
    double z
    byte[] content
    Date dateCreated
    Date lastUpdated

/* Transients */
    static transients = ['file']

/* Relations */
    static belongsTo = [project: Project]

/* Constraints */
    static constraints = {
        name(blank: false, unique: 'project')
        content(maxSize: 120000)
    }

/* Mappings */
    static mapping = {
    }

/* Hooks */

/* Named queries */

/* Transient Methods */

    File getFile() {
        return ShapeUtil.createBrepFile(content, "${project.name}_${name}_")
    }

/* Methods */

/* Static Methods */
}

