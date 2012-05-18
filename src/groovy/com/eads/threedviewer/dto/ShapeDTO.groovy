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

    String color
    List vertices = []
    List faces = []
    List edges = []
    List<ShapeGroup> groups

    ShapeDTO(String fileName) {
        List vertices = []
        List faces = []
        List edges = []

        UNVParser parser = new UNVParser()
        parser.parse(new BufferedReader(new FileReader(fileName)))
        parser.groupNames.each {String groupName ->
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
        }
        parser.nodesCoordinates.each {nodeCoordinate ->
            vertices << nodeCoordinate
        }

        this.groups = parser.groupInfo
        this.vertices = vertices
        this.edges = edges
        this.faces = faces
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

    File createUnvFile(List<ShapeGroup> shapeGroups = []) {
        String result = createFormattedContent(shapeGroups)
        return ShapeUtil.createUnvFile(result)
    }

    String createFormattedContent(List<ShapeGroup> shapeGroups = []) {
        String result = createFormattedVertices() + ls
        result += createFormattedFaces() + ls
        result += createFormattedGroupInfo(shapeGroups)
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

    String createFormattedGroupInfo(List<ShapeGroup> shapeGroups = []) {
        String result = ShapeUtil.entitiesBeginning
        result += readFormattedGroup(shapeGroups)
        result += ShapeUtil.end
        return result
    }

    String readFormattedGroup(List<ShapeGroup> shapeGroups = []) {
        String result = ''
        shapeGroups = shapeGroups ?: groups
        if (shapeGroups) {
            shapeGroups.eachWithIndex {ShapeGroup shapeGroup, int index ->
                result += readFormattedGroup(shapeGroup, index + 1) + ls
            }
        }
        log.info "Created formatted entities info"
        return result
    }

    String readFormattedGroup(ShapeGroup group, int index) {
        String result = AppUtil.createFormatI10List([index, 0, 0, 0, 0, 0, 0, group.entityCount]).join('') + "${ls}${group.name}${ls}"
        result += group.values.collect {List value ->
            AppUtil.createFormatI10List([8, value.first(), 0, 0, 8, value.last(), 0, 0]).join('')
        }.join(ls)
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

    ShapeGroup getGroupByName(String name) {
        return groups.find {it.name == name}
    }

}