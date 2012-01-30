import com.eads.threedviewer.util.MetaClassHelper

class BootStrap {

    def bootStrapService

    def init = { servletContext ->
        MetaClassHelper.enrichClasses()

        bootStrapService.bootstrapData()
    }
    def destroy = {

    }
}
