package com.eads.threedviewer

import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString
import com.eads.threedviewer.dto.ShapeDTO

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
        result['color'] = color
        return result
    }

/* Methods */

    CADMeshObject createSubMesh(ShapeDTO shapeDTO) {
        CADMeshObject cadMeshObject = new CADMeshObject(project: project, name: "${name}_${shapeDTO.groupName}", groupName: shapeDTO.groupName, parent: this,
                deflection: deflection, size: size, type: type)
        return cadMeshObject
    }

/* Static Methods */
}

