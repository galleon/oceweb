package com.eads.threedviewer

import com.eads.threedviewer.co.ShapeCO
import com.eads.threedviewer.dto.ShapeDTO

class ProjectService {

    CADObject addCADObject(ShapeCO co) {
        CADObject cadObject
        if (co.validate()) {
            cadObject = co.findOrCreateCADObject()
            Project project = co.project
            ShapeDTO shapeDTO = co.shapeDTO
            cadObject = saveCADObject(cadObject, shapeDTO, co.file)
            project.addToCadObjects(cadObject)
            project.save()

        } else {
            log.info co.errors
        }
        return cadObject
    }

    CADObject saveCADObject(CADObject cadObject, ShapeDTO shapeDTO, File file = null) {
        cadObject.save()
        saveBrepFileOnFileSystem(cadObject, file)
        return cadObject
    }

    File saveBrepFileOnFileSystem(CADObject cadObject, File file) {
        File brepFile
        if (cadObject && file) {
            brepFile = saveFileOnFileSystem(file, cadObject.brepFilePath)
            if (!brepFile.exists()) {
                throw new RuntimeException("Not able to create brep file")
            }
        }
        return brepFile
    }

    File saveFileOnFileSystem(File file, String filePath) {
        File newFile = new File(filePath)
        if (!newFile.parentFile.exists()) {
            log.info "creating parent file ${newFile.parentFile}"
            newFile.parentFile.mkdirs()
        }
        file.renameTo(newFile)
        log.info "Renaming file ${file.path} to ${filePath}"
        file.delete()
        return newFile
    }

}