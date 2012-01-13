package com.eads.threedviewer

import org.jcae.opencascade.jni.BRepPrimAPI_MakeBox
import org.jcae.opencascade.jni.TopoDS_Shape
import org.jcae.opencascade.jni.BRep_Builder
import occmeshextractor.OCCMeshExtractor
import org.jcae.opencascade.jni.BRepTools

class UtilController {

    def index() {

    }

    def three() {
        double[] p1 = [1, 1, 1]
        double[] p2 = [2, 2, 2]

        BRepPrimAPI_MakeBox box = new BRepPrimAPI_MakeBox(p1, p2)
        BRepTools.write(box.shape(), "box.brep")
        TopoDS_Shape shape = BRepTools.read("box.brep", new BRep_Builder())
        OCCMeshExtractor ome = new OCCMeshExtractor(shape)

        def f
        def e
        def v
        List nodes = ome.edges.collect {
            e = new OCCMeshExtractor.EdgeData(it)
            e.load()
            e.nodes
        }
        List polys = ome.faces.collect {
            f = new OCCMeshExtractor.FaceData(it, false)
            f.load()
            f.polys
        }
        println "Nodes----${nodes.flatten()}-----------"
        println "Polys----${polys.flatten()}-----------"

    }

    def philo = {}
    def scene={}

}