package com.eads.threedviewer.co

class UserLoginCO {
    String username
    String password

    static constraints = {
        password(blank: false)
        username(blank: false)
    }
}
