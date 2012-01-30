package com.eads.threedviewer

import com.eads.threedviewer.util.RequiredSession

@RequiredSession
class ProjectController {

    def index = {
        [projects: User.loggedInUser.projects]
    }
}