package com.eads.threedviewer

class ProjectController {

    def index(Long id) {
        List<Project> projects = Project.list()
        [projects: projects, project: id ? Project.read(id) : (projects ? projects.first() : null)]
    }

    def listCadObjects(Long id) {
        Project project = id ? Project.read(id) : null
        render(template: 'cadObjects', model: [project: project])
    }

}