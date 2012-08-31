package com.eads.threedviewer

import com.eads.threedviewer.dto.ShapeDTO
import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString

@ToString(includeNames = true, includeFields = true, excludes = 'dateCreated,lastUpdated,metaClass,content')
@EqualsAndHashCode
class CADMeshObject extends CADObject {
/* Dependency Injections (e.g. Services) */

/* Fields */
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
        return result
    }

/* Methods */

    ShapeDTO readCoordinates() {
        ShapeDTO shapeDTO = new ShapeDTO()
        File file = new File(unvFilePath)
        if (file.exists()) {
            shapeDTO = new ShapeDTO(unvFilePath)
        } else {
            if (hasParentMesh()) {
                log.info "Creating group file from parent file and moving it to ${unvFilePath}"
                shapeDTO = new ShapeDTO(parent.readCoordinates(), name)
                file = shapeDTO.createUnvFile()
                File folder = new File(filesFolderPath)
                if (!folder.exists()) {
                    folder.mkdirs()
                }
                file.renameTo(unvFilePath)
            }
        }
        String color = '0x' + Double.toHexString(Math.random() * 0xffffff).tokenize('.').last().toString().substring(0,6)
        shapeDTO.color = color
        return shapeDTO
    }

/* Static Methods */
}

