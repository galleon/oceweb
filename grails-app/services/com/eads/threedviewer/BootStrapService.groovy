package com.eads.threedviewer

class BootStrapService {

    Project createProject() {
        Project project = new Project(name: "project")
        return project.save()
    }

    void bootstrapData() {
        if (!Project.count()) {
            createProject()
            log.info "Created project ${Project.count()}"
        }

    }
}
