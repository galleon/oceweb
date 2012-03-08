package com.eads.threedviewer.util

import groovy.util.logging.Log
import occmeshextractor.OCCMeshExtractor
import org.jcae.opencascade.jni.BRepTools
import org.jcae.opencascade.jni.BRep_Builder
import org.jcae.opencascade.jni.TopoDS_Shape
import com.eads.threedviewer.dto.ShapeDTO

@Log
class ShapeUtil {

    public static Map getDefaultData() {
        return ['metadata': ['formatVersion': 3, 'generatedBy': 'tog'], 'scale': 10, 'materials': [], 'morphTargets': [], 'normals': [], 'colors': [], 'uvs': [[]]]
    }

    public static Map getData(TopoDS_Shape shape) {
        ShapeDTO shapeDTO = new ShapeDTO(shape)
        Map data = getData(shapeDTO)
        data['wireframe'] = false;
        return data
    }

    public static Map getData(ShapeDTO shapeDTO) {
        Map data = defaultData
        data['edges'] = shapeDTO.edges
        data['faces'] = shapeDTO.faces;
        data['vertices'] = shapeDTO.vertices;
        return data
    }

    public static Map getData(File file) {
        return getData(getShape(file))
    }

    public static Map getData(String filePath) {
        Map data = defaultData

        UNVParser parser = new UNVParser()
        parser.parse(new BufferedReader(new FileReader(filePath)))
        List<Integer> groupNames = parser.groupNames.collect {it.toInteger()}.toList()
        List vertices = []
        List faces = []
        List edges = []

        groupNames.each {Integer groupId ->
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
        }

        parser.nodesCoordinates.each {nodeCoordinate ->
            vertices << nodeCoordinate
        }

        data['vertices'] = vertices;
        data['faces'] = faces;
        data['edges'] = edges;
        data['wireframe'] = true;
        return data
    }

    public static File createBrepFile(byte[] content, String prefix = '') {
        File file = File.createTempFile("${prefix ?: 'temp'}", ".brep")
        file.bytes = content
        log.info "File created ${file.path}"
        return file
    }

    public static File createUnvFile(byte[] content, String prefix = '') {
        File file = File.createTempFile("${prefix ?: 'temp'}", ".unv")
        file.bytes = content
        log.info "File created ${file.path}"
        return file
    }

    public static TopoDS_Shape getShape(byte[] content) {
        return getShape(createBrepFile(content))
    }

    public static TopoDS_Shape getShape(File file) {
        TopoDS_Shape shape = getShape(file.path)
        file.delete()
        return shape
    }

    public static TopoDS_Shape getShape(String filePath) {
        return BRepTools.read(filePath, new BRep_Builder())
    }

    public static File getFile(TopoDS_Shape shape, String prefix) {
        File file = File.createTempFile(prefix, ".brep")
        BRepTools.write(shape, file.path)
        return file
    }

}
