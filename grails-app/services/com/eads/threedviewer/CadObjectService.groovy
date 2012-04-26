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
        cadObject = cadObject.save()
        if (!cadObject) {
            logErrors(cadObject)
        }
        return cadObject
    }

    CADObject saveCADObjectAndBrepFile(CADObject cadObject, File file) {
        cadObject = save(cadObject)
        if (cadObject) {
            saveFileOnFileSystem(cadObject?.brepFilePath, file)
        }
        return cadObject
    }

    CADMeshObject saveCADObjectAndUnvFile(CADMeshObject cadObject, File file) {
        cadObject = save(cadObject) as CADMeshObject
        if (cadObject) {
            saveFileOnFileSystem(cadObject?.unvFilePath, file)
        }
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
        List<ShapeDTO> shapeDTOs = ShapeUtil.getUnvGroups(cadMeshObject.unvFilePath)
        shapeDTOs.each {ShapeDTO shapeDTO ->
            log.info "creating mesh sub object for groupName ${shapeDTO.groupName} entitycount ${shapeDTO.entitiesCount}"
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
        CADMeshObject subCadMeshObject = new CADMeshObject(parent: cadMeshObject, project: cadMeshObject.project, name: "${shapeDTO.groupName}", size: cadMeshObject.size,
                deflection: cadMeshObject.deflection, type: cadMeshObject.type)
        return subCadMeshObject
    }


    CADMeshObject updateMesh(MeshCO co) {
        CADMeshObject cadObject = co.findOrCreateCADObject() as CADMeshObject
        cadObject.save()
    }

    void delete(Set<Long> ids) {
        List<CADObject> cadObjects = ids ? CADObject.getAll(ids.toList()) : []
        cadObjects.each {CADObject cadObject ->
            delete(cadObject)
        }
    }

    void delete(CADObject cadObject) {
        Project project = cadObject.project
        List childrenList = CADObject.findAllByParent(cadObject)
        String folderPath = cadObject.filesFolderPath
        if (cadObject.isMesh()) {
            if (childrenList) {
                log.info "Deleting child objects"
                List<String> folderPaths = childrenList*.filesFolderPath
                childrenList.each {CADObject child ->
                    deleteAndRemoveFromProject(project, child)
                }
                folderPaths.each {
                    fileService.removeFolder(it)
                }
            }
        } else {
            log.info "Setting parents to null"
            childrenList*.parent = null
        }
        deleteAndRemoveFromProject(project, cadObject)
        fileService.removeFolder(folderPath)
    }

    void deleteAndRemoveFromProject(Project project, CADObject cadObject) {
        log.info "Deleting cadObject ${cadObject.id}"
        project.removeFromCadObjects(cadObject)
        cadObject.delete()
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

    CADMeshObject merge(List<CADMeshObject> cadMeshObjects) {
        CADMeshObject cadMeshObject
        if (cadMeshObjects) {
            CADMeshObject firstObject = cadMeshObjects.first()
            CADObject parentCadObject = firstObject.parent
            Project project = firstObject.project
            String name = cadMeshObjects*.name.join("_")

            List<ShapeDTO> shapeDTOs = cadMeshObjects*.readCoordinates()
            ShapeDTO shapeDTO = parentCadObject.readCoordinates()
            File file = ShapeUtil.createUnvFile(new ShapeDTO(setVertices(shapeDTOs, shapeDTO.vertices), name))
            cadMeshObject = new CADMeshObject(name: name, project: project, type: ShapeType.MESH, deflection: 0, size: 0, parent: parentCadObject)
            if (cadMeshObject) {
                log.info "CADMesh Object created successfuly"
                cadMeshObjects.each {CADObject cadObject ->
                    delete(cadObject)
                }
            }
            cadMeshObject = saveCADObjectAndUnvFile(cadMeshObject, file)
        }
        return cadMeshObject
    }

    void logErrors(CADObject cadObject) {
        log.info "Error while saving ${cadObject} -:"
        cadObject.errors.allErrors.each {
            log.info "${it}"
        }
    }

    File generateUnv(CADMeshObject cadMeshObject) {
        File file
        List<CADMeshObject> cadMeshObjects = cadMeshObject.subCadObjects
        if (cadMeshObjects) {
            ShapeDTO shapeDTO = cadMeshObject.readCoordinates()
            List<ShapeDTO> shapeDTOs = setVertices(cadMeshObjects*.readCoordinates(), shapeDTO.vertices)
            file = ShapeUtil.createUnvFile(shapeDTOs)
        } else {
            file = cadMeshObject.findUnvFile()
        }
        return file
    }

    List<ShapeDTO> setVertices(List<ShapeDTO> shapeDTOs, List vertices) {
        shapeDTOs.each {ShapeDTO shapeDTO ->
            shapeDTO.vertices = vertices
        }
        return shapeDTOs
    }

}