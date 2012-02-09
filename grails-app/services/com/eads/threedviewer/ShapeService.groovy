package com.eads.threedviewer

import com.eads.threedviewer.util.ShapeUtil
import org.jcae.opencascade.jni.TopAbs_ShapeEnum
import org.jcae.opencascade.jni.TopExp_Explorer
import org.jcae.opencascade.jni.TopoDS_Shape
import com.eads.threedviewer.co.BooleanOperationCO
import org.jcae.opencascade.jni.*
import com.eads.threedviewer.enums.Operation

class ShapeService {

    List<CADObject> saveSubCadObjects(CADObject cadObject) {
        List<CADObject> cadObjects = []
        if (cadObject) {
            explode(cadObject).eachWithIndex {TopoDS_Shape shape, int index ->
                CADObject currentObject = saveSubCadObjects(cadObject, "${cadObject.name}_${index}", shape)
                if (currentObject) {
                    cadObjects.add(currentObject)
                }
            }
        }

        return cadObjects
    }

    List<TopoDS_Shape> explode(CADObject cadObject) {
        TopoDS_Shape shape = ShapeUtil.getShape(cadObject.content)
        return explode(shape, cadObject)
    }

    List<TopoDS_Shape> explode(TopoDS_Shape shape, CADObject cadObject) {
        TopExp_Explorer explorer = new TopExp_Explorer();
        int index = 1
        List<TopoDS_Shape> shapes = []

        for (explorer.init(shape, TopAbs_ShapeEnum.FACE); explorer.more(); explorer.next()) {
            shapes.add(explorer.current())
            index++
        }
        return shapes
    }

    CADObject saveSubCadObjects(CADObject cadObject, String name, TopoDS_Shape currentShape) {
        CADObject subCadObject = new CADObject(name: name, project: cadObject.project, content: ShapeUtil.getFile(currentShape,
                cadObject.name).bytes, parent: cadObject)
        return subCadObject.save()
    }

    CADObject createBooleanObject(BooleanOperationCO co){
        CADObject cadObject1 = CADObject.get(co.object1)
        CADObject cadObject2 = CADObject.get(co.object2)
        TopoDS_Shape shape1 = cadObject1.shape
        TopoDS_Shape shape2 = cadObject2.shape
        def object
        if(co.operation == Operation.FUSE.toString()){
            object = new BRepAlgoAPI_Fuse(shape1, shape2);
        }
        else if(co.operation == Operation.COMMON.toString()){
            object = new BRepAlgoAPI_Common(shape1, shape2);
        }
        else if(co.operation == Operation.CUT.toString()){
            object = new BRepAlgoAPI_Cut(shape1, shape2)
        }
        File contentForObject = ShapeUtil.getFile(object.shape(), co.operation)
        CADObject cadObject = new CADObject(name: co.name, content: contentForObject.bytes, project: cadObject1.project)
        return cadObject.save(flush: true)
    }
}