package com.eads.threedviewer.util

import groovy.util.logging.Log
import occmeshextractor.OCCMeshExtractor
import org.jcae.opencascade.jni.BRepTools
import org.jcae.opencascade.jni.BRep_Builder
import org.jcae.opencascade.jni.TopoDS_Shape

@Log
class ShapeUtil {

    public static Map getData(TopoDS_Shape shape) {
        Map data = ['metadata': ['formatVersion': 3, 'generatedBy': 'tog'], 'scale': 10, 'materials': [], 'morphTargets': [], 'normals': [], 'colors': [], 'uvs': [[]], 'edges': []]
        OCCMeshExtractor ome = new OCCMeshExtractor(shape)
        int noffset = 0
        List vertices = []
        List faces = []
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
        data['faces'] = faces;
        data['vertices'] = vertices;
        return data
    }

    public static Map getData(File file) {
        return getData(getShape(file))
    }

    public static Map getMeshEdges(String filePath) {
        Map data = ['metadata': ['formatVersion': 3, 'generatedBy': 'tog'], 'scale': 10, 'materials': [], 'morphTargets': [], 'normals': [], 'colors': [], 'uvs': [[]], 'edges': []]

        UNVParser unvParser = new UNVParser()
        unvParser.parse(new BufferedReader(new FileReader(filePath)));
        List vertices = []

        unvParser.nodesCoordinates.each {nodeCoordinate ->
            vertices << nodeCoordinate
        }
        data['vertices'] = vertices;
        return data
    }


    public static File createBrepFile(byte[] content, String prefix = '') {
        File file = File.createTempFile("${prefix ?: 'temp'}", ".brep")
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
