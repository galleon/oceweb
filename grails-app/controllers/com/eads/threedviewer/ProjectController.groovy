package com.eads.threedviewer

class ProjectController {

    def index(String name) {
        List<Project> projects = Project.list()
        [projects: projects, project: name ? Project.findOrSaveWhere([name: name]) : (projects ? projects.first() : null),shapeId:params.shapeId]
    }

    def listCadObjects(Long id) {
        Project project = id ? Project.read(id) : null
        render(template: 'cadObjects', model: [project: project, projects: Project.list()])
    }
    
    def delete(String name, Long id){
        Project project = id ? Project.get(id) : null
        if (project){
            project.delete()
        }
        redirect(action: 'index')
    }
}