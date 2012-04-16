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
        childrenList*.parent = null
        cadObject.delete()
        fileService.removeFolder(folderPath)
    }

    File saveBrepFileOnFileSystem(CADObject cadObject, File file) {
        File brepFile
        if (cadObject && file) {
            brepFile = fileService.saveFileOnFileSystem(file, cadObject.brepFilePath)
            if (!brepFile.exists()) {
                throw new RuntimeException("Not able to create brep file")
            }
        }
        return brepFile
    }
}
