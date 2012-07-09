package com.eads.threedviewer

class ProjectController {
    def grailsApplication
    def index(String name) {
        Project project = name ? Project.findOrSaveWhere([name: name]) : null
        List<Project> projects = Project.list()
        [projects: projects, project: project ?: (projects ? projects.first() : null)]
    }

    def listCadObjects(Long id) {
        Project project = id ? Project.read(id) : null
        render(template: 'cadObjects', model: [project: project, projects: Project.list()])
    }

    def delete(Long id) {
        Project project = id ? Project.get(id) : null
        if (project) {
            Project.withTransaction{project.delete();
        }

        }
        redirect(action: 'index',params: ['name','project1'])
    }

    def changeScale(){
        float scale = params.scale != null && !params.scale.equals('')? params.scale as Float: 10;
        grailsApplication.config.scale.size = scale;
        redirect(action: 'index')
    }
}