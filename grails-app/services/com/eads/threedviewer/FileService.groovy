package com.eads.threedviewer

class FileService {

    File saveFileOnFileSystem(File file, String filePath) {
        File newFile = new File(filePath)
        if (!newFile?.parentFile?.exists()) {
            log.info "creating parent file ${newFile.parentFile}"
            newFile.parentFile.mkdirs()
        }
        newFile.bytes = file?.bytes
        log.info "Moving file ${file?.path} to ${filePath} file size is ${file?.bytes?.size()}"
        file.delete()
        return newFile
    }

    void removeFolder(String folderPath) {
        File file = new File(folderPath)
        if (file.exists()) {
            log.info "Deleting file ${folderPath}"
            file.deleteDir()
        } else {
            log.info "Folder ${folderPath} not found for deletion"
        }
    }
}
