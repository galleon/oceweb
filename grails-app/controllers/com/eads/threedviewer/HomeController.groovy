package com.eads.threedviewer

import com.eads.threedviewer.co.UserLoginCO


class HomeController {

    def emailService

    def index = {
        if (User.loggedInUser) {
            forward(controller: 'util')
        } else {
            [user: new UserLoginCO()]
        }
    }

    def login = {UserLoginCO userLoginCO ->
        if (userLoginCO.validate()) {
            User user = User.getUser(userLoginCO)
            if (user) {
                User.setLoggedInUser(user.id)
            }
            else {
                userLoginCO.errors.rejectValue("username", "user.invalid.password")
            }
        }
        if (userLoginCO.hasErrors()) {
            render(view: 'index', model: [user: userLoginCO])
        } else {
            redirect(controller: 'util')
        }
    }

    def logout = {
        session.invalidate()
        forward(action: 'index')
    }

}