package com.eads.threedviewer.util

import occmeshextractor.OCCMeshExtractor
import org.jcae.opencascade.jni.TopoDS_Shape

class ShapeUtil {

    public static Map getData( TopoDS_Shape shape) {
        Map data = ['metadata': ['formatVersion': 3, 'generatedBy': 'tog'], 'scale': 10, 'materials': [], 'morphTargets': [], 'normals': [], 'colors': [], 'uvs': [[]], 'edges': []]
        OCCMeshExtractor ome = new OCCMeshExtractor(shape)
        int noffset = 0
        OCCMeshExtractor.FaceData f
        List vertices = []
        List faces = []
        ome.faces.each {
            f = new OCCMeshExtractor.FaceData(it, false)
            f.load()
            int n = 0
            f.nodes.each {
                vertices << it
                n++
            }
            int p = 0
            def npts
            f.polys.each {
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

}
