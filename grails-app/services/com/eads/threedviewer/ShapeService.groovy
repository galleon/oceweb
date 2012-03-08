package com.eads.threedviewer

import com.eads.threedviewer.co.BooleanOperationCO
import com.eads.threedviewer.enums.Operation
import com.eads.threedviewer.enums.ShapeType
import com.eads.threedviewer.util.ShapeUtil
import org.jcae.opencascade.jni.*
import com.eads.threedviewer.dto.ShapeDTO

class ShapeService {

    def projectService

    List<CADObject> saveSubCadObjects(CADObject cadObject, TopAbs_ShapeEnum shapeType) {
        List<CADObject> cadObjects = []
        if (cadObject) {
            explode(cadObject, shapeType).eachWithIndex {TopoDS_Shape shape, int index ->
                File file = ShapeUtil.getFile(shape, cadObject.name)
                ShapeDTO shapeDTO = new ShapeDTO(shape)
                CADObject currentObject = saveSubCadObjects(cadObject, "${cadObject.name}_${index}", file.bytes, shapeDTO, shapeType)
                file.delete()
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

    CADObject saveSubCadObjects(CADObject cadObject, String name, byte[] content, ShapeDTO shapeDTO, TopAbs_ShapeEnum type) {
        CADObject subCadObject = new CADExplodeObject(name: name, project: cadObject.project, parent: cadObject, type: ShapeType.EXPLODE, explodeType: type, content: content)
        return projectService.saveCADObject(subCadObject, shapeDTO)
    }

    CADObject saveCADObject(BooleanOperationCO co) {
        CADObject cadObject1 = co.CADObject1
        TopoDS_Shape shape1 = cadObject1.shape
        TopoDS_Shape shape2 = co.CADObject2.shape
        BRepAlgoAPI_BooleanOperation object = getOperationInstance(co, shape1, shape2)
        return saveCADObject(co, object.shape(), cadObject1.project)
    }

    CADObject saveCADObject(BooleanOperationCO co, TopoDS_Shape shape, Project project) {
        ShapeDTO shapeDTO = new ShapeDTO(shape)
        File file = ShapeUtil.getFile(shape, co.operation)
        CADObject cadObject = saveCADObject(co.name, file.bytes, shapeDTO, project)
        file.delete()
        return cadObject
    }

    CADObject saveCADObject(String name, byte[] content, ShapeDTO shapeDTO, Project project) {
        CADObject cadObject = new CADObject(name: name, content: content, project: project, type: ShapeType.COMPOUND)
        return projectService.saveCADObject(cadObject, shapeDTO)
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
}