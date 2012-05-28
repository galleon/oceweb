import com.eads.threedviewer.CADObject

class BootStrap {

    def init = { servletContext ->
        File file = CADObject.findOrCreateFolder()
        if (!file.exists()) {
            log.info "Folder for storing files not created at ${CADObject.folderPath}, it will cause application to misbehave."
        }
        file = new File("versionInfo.txt")
        if (!file.exists()) {
            Process process = "git log -1".execute()
            file.text = process.getText()
            log.info "Adding version number to the application"
        }
    }
    def destroy = {

    }
}
