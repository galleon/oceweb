package com.eads.threedviewer

import org.jcae.opencascade.jni.TopAbs_ShapeEnum
import org.jcae.opencascade.jni.TopExp_Explorer
import org.jcae.opencascade.jni.TopoDS_Shape

import org.jcae.opencascade.jni.*

import com.eads.threedviewer.co.BooleanOperationCO
import com.eads.threedviewer.enums.Operation
import com.eads.threedviewer.enums.ShapeType
import com.eads.threedviewer.util.ShapeUtil

class ShapeService {

    List<CADObject> saveSubCadObjects(CADObject cadObject, TopAbs_ShapeEnum shapeType) {
        List<CADObject> cadObjects = []
        if (cadObject) {
            explode(cadObject, shapeType).eachWithIndex {TopoDS_Shape shape, int index ->
                CADObject currentObject = saveSubCadObjects(cadObject, "${cadObject.name}_${index}", shape)
                if (currentObject) {
                    cadObjects.add(currentObject)
                }
            }
        }

        return cadObjects
    }

    List<TopoDS_Shape> explode(CADObject cadObject, TopAbs_ShapeEnum shapeType) {
        TopoDS_Shape shape = ShapeUtil.getShape(cadObject.content)
        return explode(shape, shapeType)
    }

    List<TopoDS_Shape> explode(TopoDS_Shape shape, TopAbs_ShapeEnum shapeType) {
        TopExp_Explorer explorer = new TopExp_Explorer();
        int index = 1
        List<TopoDS_Shape> shapes = []

        for (explorer.init(shape, shapeType); explorer.more(); explorer.next()) {
            shapes.add(explorer.current())
            index++
        }
        return shapes
    }

    CADObject saveSubCadObjects(CADObject cadObject, String name, TopoDS_Shape currentShape) {
        File file = ShapeUtil.getFile(currentShape, cadObject.name)
        CADObject subCadObject = new CADObject(name: name, project: cadObject.project, content: file.bytes, parent: cadObject, type: ShapeType.EXPLODE)
        file.delete()
        return subCadObject.save()
    }

    CADObject createBooleanObject(BooleanOperationCO co) {
        CADObject cadObject1 = co.CADObject1
        TopoDS_Shape shape1 = cadObject1.shape
        TopoDS_Shape shape2 = co.CADObject2.shape
        BRepAlgoAPI_BooleanOperation object = getOperationInstance(co, shape1, shape2)

        File file = ShapeUtil.getFile(object.shape(), co.operation)
        CADObject cadObject = saveCADObject(co.name, file.bytes, cadObject1.project)
        file.delete()
        return cadObject
    }

    CADObject saveCADObject(String name, byte[] content, Project project) {
        CADObject cadObject = new CADObject(name: name, content: content, project: project, type: ShapeType.COMPOUND)
        return cadObject.save(flush: true)
    }

    private BRepAlgoAPI_BooleanOperation getOperationInstance(BooleanOperationCO co, TopoDS_Shape shape1, TopoDS_Shape shape2) {
        def object
        if (co.operation == Operation.FUSE.toString()) {
            object = new BRepAlgoAPI_Fuse(shape1, shape2);
        }
        else if (co.operation == Operation.COMMON.toString()) {
            object = new BRepAlgoAPI_Common(shape1, shape2);
        }
        else if (co.operation == Operation.CUT.toString()) {
            object = new BRepAlgoAPI_Cut(shape1, shape2)
        }
        return object
    }

    public static getCubeInfo(CADCubeObject cadObject){
        Map result = [:]
        result.name = cadObject.name
        result.type = cadObject.type.toString()
        result.x = cadObject.x
        result.y = cadObject.y
        result.z = cadObject.z
        result.x1 = cadObject.x1
        result.y1 = cadObject.y1
        result.z1 = cadObject.z1
        return result
    }

    public static getConeInfo(CADConeObject cadObject){
        Map result = [:]
        result.name = cadObject.name
        result.type = cadObject.type.toString()
        result.x = cadObject.x
        result.y = cadObject.y
        result.z = cadObject.z
        result.baseRadius = cadObject.baseRadius
        result.height = cadObject.height
        return result
    }

    public static getCylinderInfo(CADCylinderObject cadObject){
        Map result = [:]
        result.name = cadObject.name
        result.type = cadObject.type.toString()
        result.x = cadObject.x
        result.y = cadObject.y
        result.z = cadObject.z
        result.radius = cadObject.radius
        result.height = cadObject.height
        return result
    }

    public static getSphereInfo(CADSphereObject cadObject){
        Map result = [:]
        result.name = cadObject.name
        result.type = cadObject.type.toString()
        result.x = cadObject.x
        result.y = cadObject.y
        result.z = cadObject.z
        result.radius = cadObject.radius
        return result
    }
}