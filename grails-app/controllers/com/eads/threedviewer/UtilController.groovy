package com.eads.threedviewer

import org.jcae.opencascade.jni.BRepPrimAPI_MakeBox
import org.jcae.opencascade.jni.BRepTools
import org.jcae.opencascade.jni.BRep_Builder
import occmeshextractor.OCCMeshExtractor

class UtilController {

    def index() {
        double[] p1 = [5, 5, 5]
        double[] p2 = [-5, -5, -5]

        BRepPrimAPI_MakeBox box = new BRepPrimAPI_MakeBox(p1, p2)
        BRepTools.write(box.shape(), "difference.brep")
        def difference = BRepTools.read("difference.brep", new BRep_Builder())
        def ome = new OCCMeshExtractor(difference)
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
        def f1 = new FileWriter("difference.obj")
        def f2 = new FileWriter("part2.txt")
        println "Size ${ome.faces.size()}"
        ome.faces.each {
            f = new OCCMeshExtractor.FaceData(it, false)
            f.load()
            def n = 0
            println "========================================="
            println "---vertices-----" + f.vertices
            println "---poly-----" + f.polys
            println "---Nodes-----" + f.nodes
            println "---No of lines-----" + f.nbrOfLines
            println "---No of polys-----" + f.nbrOfPolys
            println "---No of vertices-----" + f.nbrOfVertices
            f.nodes.each {
                if (n % 3 == 0) f1.write("v")
                f1.write(" ${it}")
                if (n % 3 == 2) f1.write("\n")
                n++
            }
            def p = 0
            def npts
            f.polys.each {
                if (p == 0) {
                    f2.write("f")
                    npts = it
                } else {
                    f2.write(" ${it + noffset}")
                    if (p == npts) {
                        f2.write("\n")
                        p = -1
                    }
                }
                p++
            }
            noffset += n / 3
        }
        f2.close()
        f2 = new File("part2.txt")
        println "File " + f2.text.replace("f ", "").replace(" ", ",")
        f2.eachLine {line ->
            f1.append("${line}\n")
        }
        f1.close()
        f2.delete()

    }
}

/*
{

    "metadata": { "formatVersion" : 3, "generatedBy": "Blender 2.58 Exporter" },

    "scale" : 100.000000,

    "materials": [],

    "vertices": [],

    "morphTargets": [],

    "normals": [],

    "colors": [],

    "uvs": [[]],

    "faces": [2,3,1,1,3,4,7,6,5,7,5,8,12,10,11,9,10,12,15,14,13,15,13,16,18,20,19,18,17,20,22,23,21,21,23,24],

    "edges" : []


}*/
