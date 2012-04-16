package com.eads.threedviewer.dto

import org.jcae.opencascade.jni.TopoDS_Shape
import occmeshextractor.OCCMeshExtractor
import com.eads.threedviewer.CADObject
import com.eads.threedviewer.util.UNVParser

class ShapeDTO {
    Integer groupName = 0
    String color
    List vertices = []
    List faces = []
    List edges = []

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

    public static List<ShapeDTO> getUnvGroups(String unvFilePath) {
        UNVParser parser = new UNVParser()
        parser.parse(new BufferedReader(new FileReader(unvFilePath)))
        List<Integer> groupNames = parser.groupNames.collect {it.toInteger()}.toList()
        return groupNames.collect {Integer groupId -> new ShapeDTO(parser, groupId)}
    }

}
