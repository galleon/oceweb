package com.eads.threedviewer

import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString
import com.eads.threedviewer.dto.ShapeDTO
import com.eads.threedviewer.util.AppUtil

@ToString(includeNames = true, includeFields = true, excludes = 'dateCreated,lastUpdated,metaClass,content')
@EqualsAndHashCode
class CADMeshObject extends CADObject {
/* Dependency Injections (e.g. Services) */

/* Fields */
    Integer groupName
    String color
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
        result['color'] = color
        return result
    }

/* Methods */

    ShapeDTO readCoordinates() {
        return new ShapeDTO(ShapeDTO.getUnvGroups(unvFilePath))
    }

/* Static Methods */
}

