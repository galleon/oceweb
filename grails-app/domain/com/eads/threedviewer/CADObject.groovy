package com.eads.threedviewer

import com.eads.threedviewer.dto.ShapeDTO
import com.eads.threedviewer.enums.ShapeType
import com.eads.threedviewer.util.ApplicationContextHolder
import com.eads.threedviewer.util.ShapeUtil
import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString
import org.jcae.opencascade.jni.TopoDS_Shape

@ToString(includeNames = true, includeFields = true, excludes = 'dateCreated,lastUpdated,metaClass,content')
@EqualsAndHashCode
class CADObject {
/* Dependency Injections (e.g. Services) */

/* Fields */
    String name
    double x = 0
    double y = 0
    double z = 0
    CADObject parent
    ShapeType type
    Date dateCreated
    Date lastUpdated

/* Transients */
    static transients = ['subCadObjects', 'projectFolderPath', 'filesFolderPath', 'filePath', 'brepFilePath', 'unvFilePath']

/* Relations */
    static belongsTo = [project: Project]

/* Constraints */
    static constraints = {
        name(blank: false)
        x(nullable: true)
        y(nullable: true)
        z(nullable: true)
        parent(nullable: true)
    }

/* Mappings */
    static mapping = {
    }

/* Hooks */

/* Named queries */

/* Transient Methods */

    List<CADObject> getSubCadObjects() {
        return CADObject.findAllByParent(this)
    }

    String getProjectFolderPath() {
        return "${folderPath}${project.id}/"
    }

    String getFilesFolderPath() {
        return "${projectFolderPath}${id}/"
    }

    String getFilePath() {
        return "${filesFolderPath}${id}"
    }

    String getBrepFilePath() {
        return "${filePath}.brep"
    }

    String getUnvFilePath() {
        return "${filePath}.unv"
    }

/* Methods */

    TopoDS_Shape findShape() {
        return ShapeUtil.getShape(findBrepFile())
    }

    ShapeDTO readCoordinates() {
        return new ShapeDTO(findShape())
    }

    Map readData() {
        return ShapeUtil.getData(readCoordinates())
    }

    File findBrepFile() {
        return new File(brepFilePath)
    }

    File findUnvFile() {
        return new File(unvFilePath)
    }

    Boolean isType(ShapeType type) {
        return (this.type == type)
    }

    Boolean isMesh() {
        return isType(ShapeType.MESH)
    }

    byte[] readBytes() {
        findBrepFile()?.bytes
    }

/* Static Methods */

    public static String getFolderPath() {
        return ApplicationContextHolder.config.cadobject.folder.path
    }

    public static File findOrCreateFolder() {
        File file = new File(folderPath)
        if (!file.exists()) {
            file.mkdirs()
        }
        return file
    }
}

