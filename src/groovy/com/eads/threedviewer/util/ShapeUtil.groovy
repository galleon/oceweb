package com.eads.threedviewer.util

import com.eads.threedviewer.dto.ShapeDTO
import groovy.util.logging.Log
import org.jcae.opencascade.jni.BRepTools
import org.jcae.opencascade.jni.BRep_Builder
import org.jcae.opencascade.jni.TopoDS_Shape
import org.codehaus.groovy.grails.commons.ConfigurationHolder

@Log
class ShapeUtil {

    public static String ls = System.getProperty("line.separator")


    public static Map getDefaultData() {
        return ['metadata': ['formatVersion': 3, 'generatedBy': 'tog'], 'materials': [], 'morphTargets': [], 'normals': [], 'colors': [], 'uvs': [[]]]
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
        data['faces'] = shapeDTO.faces
        data['vertices'] = shapeDTO.vertices
        data['scalingFactor'] = shapeDTO.scalingFactor
        data['color'] = shapeDTO.color
        data['colors'] = shapeDTO.resultDTO ? shapeDTO.resultDTO.calculatedResults : []
        return data
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
        return "${end}${ls}${' ' * 2}${code}${ls}"
    }

    public static String getEnd() {
        return "${' ' * 4}-1"
    }

}