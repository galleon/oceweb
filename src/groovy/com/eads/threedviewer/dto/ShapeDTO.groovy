package com.eads.threedviewer.dto

import com.eads.threedviewer.util.AppUtil
import com.eads.threedviewer.util.UNVParser
import groovy.transform.ToString
import occmeshextractor.OCCMeshExtractor
import org.jcae.opencascade.jni.TopoDS_Shape
import com.eads.threedviewer.util.ShapeUtil
import groovy.util.logging.Log

@Log
@ToString(includeNames = true, includeFields = true, excludes = 'metaClass')
class ShapeDTO {
    public static String ls = System.getProperty("line.separator")

    String groupName
    String color
    List vertices = []
    List faces = []
    List edges = []

    Integer getEntitiesCount() {
        return faces ? (faces.size() / 4) : 0
    }

    ShapeDTO(List<ShapeDTO> shapeDTOs, String name) {
        vertices = shapeDTOs.first().vertices
        edges = shapeDTOs.edges.flatten()
        faces = shapeDTOs.faces.flatten()
        groupName = name
    }

    ShapeDTO(TopoDS_Shape shape) {
        List vertices = []
        List faces = []

        OCCMeshExtractor ome = new OCCMeshExtractor(shape)
        int noffset = 0
        ome.faces.each {
            OCCMeshExtractor.FaceData shapeData = new OCCMeshExtractor.FaceData(it, false)
            shapeData.load()
            int n = 0
            shapeData.nodes.each {
                vertices << it
                n++
            }
            int p = 0
            def npts
            shapeData.polys.each {
                if (p == 0) {
                    faces << 0
                    npts = it
                } else {
                    faces << it + noffset
                    if (p == npts) {
                        p = -1
                    }
                }
                p++
            }
            noffset += n / 3
        }
        this.faces = faces
        this.vertices = vertices
    }

    ShapeDTO(UNVParser parser, String groupName) {
        List vertices = []
        List faces = []
        List edges = []

        int[] triangles = parser.getTria3FromGroup(groupName)
        for (int i = 0; i < triangles.length;) {
            faces << 0
            faces << triangles[i++]
            faces << triangles[i++]
            faces << triangles[i++]
        }
        int[] quads = parser.getQuad4FromGroup(groupName)
        for (int i = 0; i < quads.length;) {
            faces << 1
            faces << quads[i++]
            faces << quads[i++]
            faces << quads[i++]
            faces << quads[i++]
        }
        int[] beams = parser.getQuad4FromGroup(groupName)
        for (int i = 0; i < beams.length;) {
            edges << beams[i++]
            edges << beams[i++]
        }

        parser.nodesCoordinates.each {nodeCoordinate ->
            vertices << nodeCoordinate
        }
        this.groupName = groupName
        this.vertices = vertices
        this.edges = edges
        this.faces = faces
    }

    File createUnvFile() {
        String result = createFormattedContent()
        return ShapeUtil.createUnvFile(result)
    }

    String createFormattedContent() {
        String result = createFormattedVertices() + ls
        result += createFormattedFaces() + ls
        result += createFormattedEntityInfo()
        return result
    }

    String createFormattedVertices() {
        String result = ShapeUtil.verticesBeginning
        result += readFormattedVertices()
        result += ShapeUtil.end
        return result
    }

    String readFormattedVertices() {
        String result = ""
        readTriangularVertices(vertices).eachWithIndex {List val, int index ->
            result += "${AppUtil.createFormatI10List([index + 1, 1, 1, 1]).join('')}${ls}${AppUtil.createFormatI25List(val).join('')}${ls}"
        }
        log.info "Created formatted vertices"
        return result
    }

    String createFormattedFaces() {
        String result = ShapeUtil.facesBeginning
        result += readFormattedFaces()
        result += ShapeUtil.end
        return result
    }

    String readFormattedFaces(Integer startPoint = 0) {
        String result = ""
        readTriangularFaces(faces).eachWithIndex {List val, int index ->
            result += "${AppUtil.createFormatI10List([index + startPoint + 1, 91, 1, 1, 1, 3]).join('')}${ls}${AppUtil.createFormatI10List(val.collect {it + 1}).join('')}${ls}"
        }
        log.info "Created formatted faces"
        return result
    }

    String createFormattedEntityInfo() {
        String result = ShapeUtil.entitiesBeginning
        result += readFormattedEntities()
        result += ShapeUtil.end
        return result
    }

    String readFormattedEntities(Integer groupNumber = 1, String groupName = '1', Integer startPoint = 0) {
        String result = AppUtil.createFormatI10List([groupNumber, 0, 0, 0, 0, 0, 0, entitiesCount]).join('') + "${ls}${groupName}${ls}"
        result += (0..((entitiesCount / 2) - 1).toInteger()).collect {[8, ((2 * it) + 1 + startPoint), 0, 0, 8, ((2 * it) + 2 + startPoint), 0, 0]}.collect {List row ->
            AppUtil.createFormatI10List(row).join('')
        }.join(ls) + "${ls}"
        log.info "Created formatted entities info"
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

}