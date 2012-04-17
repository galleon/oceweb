package com.eads.threedviewer

class CadObjectService {

    def fileService

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
}
