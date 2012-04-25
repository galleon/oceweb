package com.eads.threedviewer.util

import com.eads.threedviewer.dto.ShapeDTO
import groovy.util.logging.Log
import org.jcae.opencascade.jni.BRepTools
import org.jcae.opencascade.jni.BRep_Builder
import org.jcae.opencascade.jni.TopoDS_Shape

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
        data['color'] = shapeDTO.color;
        return data
    }

    public static Map getData(File file) {
        return getData(getShape(file))
    }

    public static File createTempBrepFile(byte[] content) {
        File file = File.createTempFile("temp", ".brep")
        file.bytes = content
        file.deleteOnExit()
        log.info "File created ${file.path} and it will be deleted on exit"
        return file
    }

    public static TopoDS_Shape getShape(byte[] content) {
        return getShape(createTempBrepFile(content))
    }

    public static TopoDS_Shape getShape(File file) {
        return getShape(file.path)
    }

    public static TopoDS_Shape getShape(String filePath) {
        return BRepTools.read(filePath, new BRep_Builder())
    }

    public static File getFile(TopoDS_Shape shape, String prefix) {
        File file = File.createTempFile(prefix, ".brep")
        BRepTools.write(shape, file.path)
        return file
    }

    public static List<ShapeDTO> getUnvGroups(String unvFilePath) {
        UNVParser parser = new UNVParser()
        parser.parse(new BufferedReader(new FileReader(unvFilePath)))
        List<Integer> groupNames = parser.groupNames.collect {it.toInteger()}.toList()
        return groupNames.collect {Integer groupId -> new ShapeDTO(parser, groupId)}
    }

    public static File createUnvFile(List<ShapeDTO> shapeDTOs) {
        return createUnvFile(new ShapeDTO(shapeDTOs))
    }

    public static File createUnvFile(ShapeDTO shapeDTO) {
        return shapeDTO.createUnvFile()
    }

    public static File createUnvFile(String result) {
        File file = File.createTempFile("result", ".unv")
        file.text = result
        return file
    }

    public static String getVerticesBeginning() {
        return getBeginning('2411')
    }

    public static String getFacesBeginning() {
        return getBeginning('2412')
    }

    public static String getEntitiesBeginning() {
        return getBeginning('2435')
    }

    public static String getBeginning(String code) {
        String ls = System.getProperty("line.separator")
        return "${end}${ls}${' ' * 2}${code}${ls}"
    }

    public static String getEnd() {
        return "${' ' * 4}-1"
    }

}