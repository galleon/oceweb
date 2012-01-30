package com.eads.threedviewer

class BootStrapService {

    User createAdminUser() {
        User user = new User(username: "admin", password: "admin", isAdmin: true)
        return user.s()
    }

    Project createProject(User user) {
        Project project = new Project(name: "project")
        user.addToProjects(project)
        user.save()
        return project
    }

    void bootstrapData() {
        User user
        if (User.count()) {
            user = User.list(max: 1).first()
        } else {
            user = createAdminUser()
            log.info "Created user ${User.count()}"
        }

        if (!Project.count()) {
            createProject(user)
            log.info "Created project ${Project.count()}"
        }

    }
}
