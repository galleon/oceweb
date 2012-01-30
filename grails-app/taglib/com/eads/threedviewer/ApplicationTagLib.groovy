package com.eads.threedviewer

class ApplicationTagLib {

    static namespace = "td"

    def ifLoggedIn = {attrs, body ->
        if (User.loggedInUser) {
            out << body()
        }
    }

    def loggedInUserName = {attrs, body ->
        User user = User.loggedInUser
        if (user) {
            out << user.username
        }
    }

    def ifNotLoggedIn = {attrs, body ->
        if (!User.loggedInUser) {
            out << body()
        }
    }

    def isAdmin = {attrs, body ->
        if (User.loggedInUser?.isAdmin) {
            out << body()
        }

    }
}
