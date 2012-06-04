package com.eads.threedviewer

import org.codehaus.groovy.grails.web.context.ServletContextHolder

class FileService {

    File saveFileOnFileSystem(File file, String filePath) {
        File newFile = new File(filePath)
        if (!newFile?.parentFile?.exists()) {
            log.info "creating parent file ${newFile.parentFile}"
            newFile.parentFile.mkdirs()
        }
        log.info "Moving file ${file?.path} to ${filePath} file size is ${file?.bytes?.size()}"
        file.renameTo(filePath)
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

    void deleteFilesExcept(File folder, File file) {
        log.info "Deleting unneccessary files and folders from ${folder.name} except ${file.name}"
        folder.eachDir {
            log.info "Deleting folder ${it.name}"
            it.deleteDir()
        }
        folder.eachFile {
            if (it.name != file.name) {
                log.info "Deleting file ${it.name}"
                it.delete()
            }
        }
    }

    File getDummySimulatedFile() {
        String fileName = ServletContextHolder.servletContext.getRealPath("/") + "coneSphere.unv"
        File simulatedFile = new File(fileName)
        File dummyFile = File.createTempFile("dummy", "")
        dummyFile.bytes = simulatedFile.bytes
        return dummyFile
    }
}
