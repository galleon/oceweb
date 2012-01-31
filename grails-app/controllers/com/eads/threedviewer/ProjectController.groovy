package com.eads.threedviewer

class ProjectController {

    def index = {
        [projects: Project.list()]
    }
}