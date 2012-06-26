import com.eads.threedviewer.CADObject
import com.eads.threedviewer.util.UNVParser
import com.eads.threedviewer.co.ResultCO
import com.eads.threedviewer.enums.ShapeType
import com.eads.threedviewer.CADMeshObject
import com.eads.threedviewer.co.SimulationCO

class BootStrap {

    def grailsApplication
    def shapeService;
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
       // readGroupUnvFile();
    }




    def destroy = {

    }
}
