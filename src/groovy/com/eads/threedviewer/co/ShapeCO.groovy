package com.eads.threedviewer.co

import org.codehaus.groovy.grails.validation.Validateable
import groovy.transform.ToString
import org.jcae.opencascade.jni.TopoDS_Shape
import occmeshextractor.OCCMeshExtractor

@Validateable
@ToString(includeNames = true, includeFields = true, excludes = 'metaClass,errors')
abstract class ShapeCO {
    String name
    double x
    double y
    double z

    static constraints = {
        name(nullable: false, blank: false)
        x(nullable: false)
        y(nullable: false)
        z(nullable: false)
    }

    abstract TopoDS_Shape getShape()

    Map getData() {
        log.info "Into get Data method"
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
