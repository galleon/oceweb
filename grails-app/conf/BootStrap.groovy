import com.eads.threedviewer.CADObject

class BootStrap {

    def grailsApplication

    def init = { servletContext ->
        File file = CADObject.findOrCreateFolder()
        if (!file.exists()) {
            log.info "Folder for storing files not created at ${CADObject.folderPath}, it will cause application to misbehave."
        }
        if (!grailsApplication.isWarDeployed()) {
            String fileName = "web-app/currentVersion.txt"
            file = new File(fileName)
            Process process = "git log -1".execute()
            file.text = process.getText()
            log.info "Adding version number to the application"
        }
    }
    def destroy = {

    }
}
