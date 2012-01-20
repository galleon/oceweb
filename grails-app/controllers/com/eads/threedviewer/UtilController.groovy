package com.eads.threedviewer

import org.jcae.opencascade.jni.BRepPrimAPI_MakeBox
import org.jcae.opencascade.jni.TopoDS_Shape
import org.jcae.opencascade.jni.BRep_Builder
import occmeshextractor.OCCMeshExtractor
import org.jcae.opencascade.jni.BRepTools

class UtilController {

    def index() {

    }

    def human(){

    }

    def box() {
        double[] p2 = [5, 5, 5]
        double[] p1 = [-5, -5, -5]

        BRepPrimAPI_MakeBox box = new BRepPrimAPI_MakeBox(p1, p2)
        BRepTools.write(box.shape(), "box.brep")
        TopoDS_Shape shape = BRepTools.read("box.brep", new BRep_Builder())
        def ome = new OCCMeshExtractor(shape)
        ome.vertices.each {
            def v = new OCCMeshExtractor.VertexData(it)
            v.load()
        }
        ome.edges.each {
            def e = new OCCMeshExtractor.EdgeData(it)
            e.load()
        }
        def noffset = 1
        def f
        List vertices = []
        List faces = []
        ome.faces.each {
            f = new OCCMeshExtractor.FaceData(it, false)
            f.load()
            def n = 0
            f.nodes.each {
                vertices << it
                n++
            }
            def p = 0
            def npts
            f.polys.each {
                if (p == 0) {
                    npts = it
                } else {
                    faces.add(it + noffset)
                    if (p == npts) {
                        p = -1
                    }
                }
                p++
            }
            noffset += n / 3
        }
//        println "vertices ${vertices}"
//        println "faces ${faces}"
    }

    def philo = {}
    def scene = {}

}