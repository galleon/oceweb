package com.eads.threedviewer

import com.eads.threedviewer.dto.ShapeDTO
import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString
import com.eads.threedviewer.util.ShapeUtil

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
        groupName(nullable: true)
        size(nullable: false)
        deflection(nullable: false)
    }

/* Mappings */
    static mapping = {
    }

/* Hooks */

/* Named queries */

/* Transient Methods */

    Map readData() {
        Map result = super.readData()
        result['wireframe'] = true
        return result
    }

/* Methods */

    ShapeDTO readCoordinates() {
        return new ShapeDTO(ShapeUtil.getUnvGroups(unvFilePath))
    }

/* Static Methods */
}

