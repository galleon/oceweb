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
    String color
    float size
    float deflection

/* Transients */

/* Relations */

/* Constraints */
    static constraints = {
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

    File createUnvFile() {
        String result = createFormattedUnv()
        File file = File.createTempFile("result", ".unv")
        file.text = result
        return file
    }

    String createFormattedUnv() {
        return createFormattedUnv(readCoordinates())
    }

    String createFormattedUnv(ShapeDTO shapeDTO) {
        String ls = System.getProperty("line.separator")
        String result = createFormattedVertices(shapeDTO.vertices) + ls
        result += createFormattedFaces(shapeDTO.faces)
        return result
    }

    String createFormattedVertices(List vertices) {
        String ls = System.getProperty("line.separator")
        String result = "${' ' * 4}-1${ls}${' ' * 2}2411${ls}"

        readTriangularVertices(vertices).eachWithIndex {List val, int index ->
            result += "${AppUtil.createFormatI10List([index + 1, 1, 1, 1]).join('')}${ls}${AppUtil.createFormatI25List(val).join('')}${ls}"
        }
        result += "${' ' * 4}-1"
        return result
    }

    String createFormattedFaces(List faces) {
        String ls = System.getProperty("line.separator")
        String result = "${' ' * 4}-1${ls}${' ' * 2}2412${ls}"

        readTriangularFaces(faces).eachWithIndex {List val, int index ->
            result += "${AppUtil.createFormatI10List([index + 1, 91, 1, 1, 1, 3]).join('')}${ls}${AppUtil.createFormatI10List(val.collect {it + 1}).join('')}${ls}"
        }
        result += "${' ' * 4}-1"
        return result
    }

    List readTriangularVertices(List vertices) {
        return AppUtil.getTriangularList(vertices)
    }

    List readTriangularFaces(List faces) {
        List modifiedFaces = []
        faces.eachWithIndex {val, index ->
            if (index % 4) {
                modifiedFaces.add(val)
            }
        }
        return AppUtil.getTriangularList(modifiedFaces)
    }

/* Static Methods */
}

