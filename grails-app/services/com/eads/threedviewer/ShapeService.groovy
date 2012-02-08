package com.eads.threedviewer

import com.eads.threedviewer.util.ShapeUtil
import org.jcae.opencascade.jni.TopAbs_ShapeEnum
import org.jcae.opencascade.jni.TopExp_Explorer
import org.jcae.opencascade.jni.TopoDS_Shape

class ShapeService {

    List<CADObject> saveSubCadObjects(Long id) {
        CADObject cadObject = id ? CADObject.get(id) : null
        return saveSubCadObjects(cadObject)

    }

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
}