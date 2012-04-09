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
    static transients = ['triangularFaces', 'triangularVertices']

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

    List getTriangularVertices() {
        return AppUtil.getTriangularList(verticesList)
    }

    List getTriangularFaces() {
        List faces = []
        facesList.eachWithIndex {val, index ->
            if (index % 4) {
                faces.add(val)
            }
        }
        return AppUtil.getTriangularList(faces)
    }

/* Methods */

    CADMeshObject createSubMesh(ShapeDTO shapeDTO) {
        CADMeshObject cadMeshObject = new CADMeshObject(project: project, name: "${name}_${shapeDTO.groupName}", groupName: shapeDTO.groupName, parent: this,
                deflection: deflection, size: size, type: type)
        return cadMeshObject
    }

    File createUnvFile() {
        String result = createFormattedUnv()
        File file = File.createTempFile("result", ".unv")
        file.text = result
        return file
    }

    String createFormattedUnv() {
        String ls = System.getProperty("line.separator");
        String result = createFormattedVertices() + ls
        result += createFormattedFaces()
        return result
    }

    String createFormattedVertices() {
        String ls = System.getProperty("line.separator")
        String result = "${' ' * 4}-1${ls}${' ' * 2}2411${ls}"

        triangularVertices.eachWithIndex {List val, int index ->
            result += "${AppUtil.createFormatI10List([index + 1, 1, 1, 1]).join('')}${ls}${AppUtil.createFormatI25List(val).join('')}${ls}"
        }
        result += "${' ' * 4}-1"
        return result
    }

    String createFormattedFaces() {
        String ls = System.getProperty("line.separator")
        String result = "${' ' * 4}-1${ls}${' ' * 2}2412${ls}"

        triangularFaces.eachWithIndex {List val, int index ->
            result += "${AppUtil.createFormatI10List([index + 1, 91, 1, 1, 1, 3]).join('')}${ls}${AppUtil.createFormatI10List(val.collect {it + 1}).join('')}${ls}"
        }
        result += "${' ' * 4}-1"
        return result
    }

/* Static Methods */
}

