import com.eads.threedviewer.CADObject

class BootStrap {

    def bootStrapService

    def init = { servletContext ->
        File file = CADObject.findOrCreateFolder()
        if (!file.exists()) {
            log.info "Folder for storing files not created at ${CADObject.folderPath}, it will cause application to misbehave."
        }
    }
    def destroy = {

    }
}
