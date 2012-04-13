package com.eads.threedviewer

import com.eads.threedviewer.enums.ShapeType
import com.eads.threedviewer.util.ShapeUtil
import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString
import org.jcae.opencascade.jni.TopoDS_Shape
import com.eads.threedviewer.dto.ShapeDTO
import com.eads.threedviewer.util.ApplicationContextHolder

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
    String vertices
    String edges
    String faces
    Date dateCreated
    Date lastUpdated

/* Transients */
    static transients = ['subCadObjects', 'shape', 'verticesList', 'edgesList', 'facesList', 'shapeDTO', 'data']

/* Relations */
    static belongsTo = [project: Project]

/* Constraints */
    static constraints = {
        name(blank: false)
        x(nullable: true)
        y(nullable: true)
        z(nullable: true)
        vertices(nullable: true, blank: true)
        faces(nullable: true, blank: true)
        edges(nullable: true, blank: true)
        parent(nullable: true)
    }

/* Mappings */
    static mapping = {
        vertices type: 'text'
        edges type: 'text'
        faces type: 'text'
    }

/* Hooks */

/* Named queries */

/* Transient Methods */

    List<CADObject> getSubCadObjects() {
        return CADObject.findAllByParent(this)
    }

    TopoDS_Shape getShape() {
        return ShapeUtil.getShape(findBrepFile())
    }

    List<Integer> getVerticesList() {
        return vertices ? vertices.tokenize(',').collect {it.toFloat()}.toList() : []
    }

    List<Integer> getEdgesList() {
        return edges ? edges.tokenize(',').collect {it.toFloat()}.toList() : []
    }

    List<Integer> getFacesList() {
        return faces ? faces.tokenize(',').collect {it.toFloat()}.toList() : []
    }

    ShapeDTO getShapeDTO() {
        return new ShapeDTO(this)
    }

    Map getData() {
        return ShapeUtil.getData(shapeDTO)
    }

/* Methods */

    File findBrepFile() {
        return new File(folderPath + "${project.id}/${id}.brep")
    }

    File findUnvFile() {
        return new File(folderPath + "${project.id}/${id}.unv")
    }

    Boolean isType(ShapeType type) {
        return (this.type == type)
    }

    Boolean isMesh() {
        return isType(ShapeType.MESH)
    }

    byte[] fetchContent() {
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

