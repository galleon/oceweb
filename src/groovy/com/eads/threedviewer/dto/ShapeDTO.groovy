package com.eads.threedviewer.dto

import org.jcae.opencascade.jni.TopoDS_Shape
import occmeshextractor.OCCMeshExtractor
import com.eads.threedviewer.CADObject
import com.eads.threedviewer.util.UNVParser
import com.eads.threedviewer.util.AppUtil
import groovy.transform.ToString

@ToString(includeNames = true, includeFields = true, excludes = 'metaClass')
class ShapeDTO {
    Integer groupName = 0
    String color
    List vertices = []
    List faces = []
    List edges = []

    Integer getEntitiesCount() {
        return faces ? (faces.size()/4) : 0
    }

    ShapeDTO(List<ShapeDTO> shapeDTOs) {
        vertices = shapeDTOs.vertices.flatten()
        edges = shapeDTOs.edges.flatten()
        faces = shapeDTOs.faces.flatten()
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

    ShapeDTO(UNVParser parser, Integer groupId) {
        List vertices = []
        List faces = []
        List edges = []

        Integer id = groupId - 1
        int[] triangles = parser.getTria3FromGroup(id)
        for (int i = 0; i < triangles.length;) {
            faces << 0
            faces << triangles[i++]
            faces << triangles[i++]
            faces << triangles[i++]
        }
        int[] quads = parser.getQuad4FromGroup(id)
        for (int i = 0; i < quads.length;) {
            faces << 1
            faces << quads[i++]
            faces << quads[i++]
            faces << quads[i++]
            faces << quads[i++]
        }
        int[] beams = parser.getQuad4FromGroup(id)
        for (int i = 0; i < beams.length;) {
            edges << beams[i++]
            edges << beams[i++]
        }

        parser.nodesCoordinates.each {nodeCoordinate ->
            vertices << nodeCoordinate
        }
        this.groupName = id
        this.vertices = vertices
        this.edges = edges
        this.faces = faces
    }

    File createUnvFile() {
        String result = createFormattedUnv()
        File file = File.createTempFile("result", ".unv")
        file.text = result
        return file
    }

    String createFormattedUnv() {
        String ls = System.getProperty("line.separator")
        String result = createFormattedVertices() + ls
        result += createFormattedFaces()
        result += createFormattedEntityInfo()
        return result
    }

    String createFormattedVertices() {
        String ls = System.getProperty("line.separator")
        String result = "${' ' * 4}-1${ls}${' ' * 2}2411${ls}"

        readTriangularVertices(vertices).eachWithIndex {List val, int index ->
            result += "${AppUtil.createFormatI10List([index + 1, 1, 1, 1]).join('')}${ls}${AppUtil.createFormatI25List(val).join('')}${ls}"
        }
        result += "${' ' * 4}-1"
        return result
    }

    String createFormattedFaces() {
        String ls = System.getProperty("line.separator")
        String result = "${' ' * 4}-1${ls}${' ' * 2}2412${ls}"

        readTriangularFaces(faces).eachWithIndex {List val, int index ->
            result += "${AppUtil.createFormatI10List([index + 1, 91, 1, 1, 1, 3]).join('')}${ls}${AppUtil.createFormatI10List(val.collect {it + 1}).join('')}${ls}"
        }
        result += "${' ' * 4}-1"
        return result
    }

    String createFormattedEntityInfo() {
        String ls = System.getProperty("line.separator")
        String result = "${ls}${' ' * 4}-1${ls}${' ' * 2}2435${ls}"
        result += AppUtil.createFormatI10List([1, 0, 0, 0, 0, 0, 0, entitiesCount]).join('') + "${ls}1${ls}"
        result += (0..((entitiesCount / 2) - 1).toInteger()).collect {[8, ((2 * it) + 1), 0, 0, 8, ((2 * it) + 2), 0, 0]}.collect {List row -> AppUtil.createFormatI10List(row).join('')}
                .join(ls) + "${ls}"
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

    public static List<ShapeDTO> getUnvGroups(String unvFilePath) {
        UNVParser parser = new UNVParser()
        parser.parse(new BufferedReader(new FileReader(unvFilePath)))
        List<Integer> groupNames = parser.groupNames.collect {it.toInteger()}.toList()
        return groupNames.collect {Integer groupId -> new ShapeDTO(parser, groupId)}
    }

}
