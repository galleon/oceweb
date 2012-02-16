package com.eads.threedviewer

class ProjectController {

    def index(String name) {
        List<Project> projects = Project.list()
        CADObject cadObject = params.shapeId ? CADObject.get(params.shapeId) : null
        Project project = cadObject ? cadObject.project : null
        project = name ? Project.findOrSaveWhere([name: name]) : project
        [projects: projects, project: project ?: (projects ? projects.first() : null), shapeId: params.shapeId]
    }

    def listCadObjects(Long id) {
        Project project = id ? Project.read(id) : null
        render(template: 'cadObjects', model: [project: project, projects: Project.list()])
    }

    def delete(Long id) {
        Project project = id ? Project.get(id) : null
        if (project) {
            project.delete()
        }
        redirect(action: 'index')
    }
}