package com.eads.threedviewer

import com.eads.threedviewer.dto.ShapeDTO
import com.eads.threedviewer.enums.ShapeType
import com.eads.threedviewer.co.ShapeCO
import org.jcae.opencascade.jni.TopAbs_ShapeEnum
import org.jcae.opencascade.jni.TopoDS_Shape
import com.eads.threedviewer.util.ShapeUtil
import com.eads.threedviewer.co.BooleanOperationCO
import org.jcae.opencascade.jni.BRepAlgoAPI_BooleanOperation
import com.eads.threedviewer.co.MeshCO

class CadObjectService {

    def fileService
    def shapeService

    CADObject save(ShapeCO co) {
        CADObject cadObject
        if (co.validate()) {
            cadObject = co.findOrCreateCADObject()
            save(co.project, cadObject)
            cadObject = saveCADObjectAndBrepFile(cadObject, co.file)
        } else {
            log.info co.errors
        }
        return cadObject
    }

    CADObject save(CADObject cadObject) {
        return save(cadObject.project, cadObject)
    }

    CADObject save(Project project, CADObject cadObject) {
        project.addToCadObjects(cadObject)
        project.save()
        return cadObject.save()
    }

    CADObject saveCADObjectAndBrepFile(CADObject cadObject, File file) {
        cadObject = cadObject.save()
        saveFileOnFileSystem(cadObject?.brepFilePath, file)
        return cadObject
    }

    CADObject saveCADObjectAndUnvFile(CADObject cadObject, File file) {
        cadObject = cadObject.save()
        saveFileOnFileSystem(cadObject?.unvFilePath, file)
        return cadObject
    }

    List<CADObject> saveSubCadObjects(CADObject cadObject, TopAbs_ShapeEnum shapeType) {
        List<CADObject> cadObjects = []
        if (cadObject) {
            shapeService.explode(cadObject, shapeType).eachWithIndex {TopoDS_Shape shape, int index ->
                File file = ShapeUtil.getFile(shape, cadObject.name)
                CADObject currentObject = saveSubCadObjects(cadObject, "${cadObject.name}_${index}", file, shapeType)
                file.delete()
                if (currentObject) {
                    cadObjects.add(currentObject)
                }
            }
        }

        return cadObjects
    }

    CADObject saveSubCadObjects(CADObject cadObject, String name, File file, TopAbs_ShapeEnum type) {
        CADObject subCadObject = new CADExplodeObject(name: name, project: cadObject.project, parent: cadObject, type: ShapeType.EXPLODE, explodeType: type)
        return saveCADObjectAndBrepFile(subCadObject, file)
    }

    CADObject saveCADObject(BooleanOperationCO co) {
        CADObject cadObject1 = co.CADObject1
        TopoDS_Shape shape1 = cadObject1.findShape()
        TopoDS_Shape shape2 = co.CADObject2.findShape()
        BRepAlgoAPI_BooleanOperation object = shapeService.getOperationInstance(co, shape1, shape2)
        return saveCADObject(co, object.shape(), cadObject1.project)
    }

    CADObject saveCADObject(BooleanOperationCO co, TopoDS_Shape shape, Project project) {
        File file = ShapeUtil.getFile(shape, co.operation)
        CADObject cadObject = saveCADObject(project, co.name, file)
        file.delete()
        return cadObject
    }

    CADObject saveCADObject(Project project, String name, File file) {
        CADObject cadObject = new CADObject(name: name, project: project, type: ShapeType.COMPOUND)
        return saveCADObjectAndBrepFile(cadObject, file)
    }


    CADMeshObject saveMesh(MeshCO co) {
        CADMeshObject cadObject = co.findOrCreateCADObject() as CADMeshObject
        cadObject = save(co.project, cadObject as CADObject)
        if (cadObject) {
            File file = shapeService.generateMeshFolder(co, cadObject.id)
            log.info "CadMeshObject saved successfuly. Moving ${file.path} to ${cadObject.filesFolderPath}"
            fileService.renameFolder(file, cadObject.filesFolderPath)
            saveSubMeshes(cadObject)
        }
        else {
            logErrors(cadObject)
        }
        return cadObject
    }

    List<CADMeshObject> saveSubMeshes(CADMeshObject cadMeshObject) {
        List<CADMeshObject> cadMeshObjects = []
        List<ShapeDTO> shapeDTOs = ShapeDTO.getUnvGroups(cadMeshObject.unvFilePath)
        shapeDTOs.each {ShapeDTO shapeDTO ->
            log.info "creating mesh sub object for entitycount ${shapeDTO.entitiesCount}"
            CADMeshObject subCadMeshObject = saveSubMesh(cadMeshObject, shapeDTO)
            if (subCadMeshObject) {
                cadMeshObjects.add(subCadMeshObject)
            }
        }
        return cadMeshObjects
    }

    CADMeshObject saveSubMesh(CADMeshObject cadMeshObject, ShapeDTO shapeDTO) {
        CADMeshObject subCadMeshObject = createSubCADMesh(cadMeshObject, shapeDTO)
        saveCADObjectAndUnvFile(subCadMeshObject, shapeDTO.createUnvFile())
        return subCadMeshObject
    }

    CADMeshObject createSubCADMesh(CADMeshObject cadMeshObject, ShapeDTO shapeDTO) {
        CADMeshObject subCadMeshObject = new CADMeshObject(parent: cadMeshObject, project: cadMeshObject.project, name: "${cadMeshObject.name}_${shapeDTO.groupName}",
                size: cadMeshObject.size, deflection: cadMeshObject.deflection, type: cadMeshObject.type, groupName: shapeDTO.groupName)
        return subCadMeshObject
    }


    CADMeshObject updateMesh(MeshCO co) {
        CADMeshObject cadObject = co.findOrCreateCADObject() as CADMeshObject
        cadObject.save()
    }

    void logErrors(CADObject cadObject) {
        log.info "Error while saving ${cadObject} -:"
        cadObject.errors.allErrors.each {
            log.info "${it}"
        }
    }

    void delete(Set<Long> ids) {
        List<CADObject> cadObjects = ids ? CADObject.getAll(ids.toList()) : []
        cadObjects.each {CADObject cadObject ->
            delete(cadObject)
        }
    }

    void delete(CADObject cadObject) {
        List childrenList = CADObject.findAllByParent(cadObject)
        String folderPath = cadObject.filesFolderPath
        if (cadObject.isMesh()) {
            List<String> folderPaths = childrenList*.filesFolderPath
            childrenList*.delete()
            folderPaths.each {
                fileService.removeFolder(it)
            }
        } else {
            childrenList*.parent = null
        }
        cadObject.delete()
        fileService.removeFolder(folderPath)
    }

    File saveFileOnFileSystem(String filePath, File file) {
        File brepFile
        if (filePath && file) {
            brepFile = fileService.saveFileOnFileSystem(file, filePath)
            if (!brepFile.exists()) {
                throw new RuntimeException("Not able to create brep file at ${filePath}")
            }
        } else {
            log.info "Not able to save file ${file.path} to ${filePath}"
        }
        return brepFile
    }

    File merge(List<CADMeshObject> cadMeshObjects) {
        return ShapeDTO.createUnvFile(cadMeshObjects*.readCoordinates())
    }
}
