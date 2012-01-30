package com.eads.threedviewer

import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString
import com.eads.threedviewer.util.SessionUtil
import com.eads.threedviewer.co.UserLoginCO

@ToString(includeNames = true, includeFields = true, excludes = 'dateCreated,lastUpdated,metaClass')
@EqualsAndHashCode

class User {

/* Dependency Injections (e.g. Services) */

/* Fields */
    String username
    String password
    boolean isAdmin = false
    Date dateCreated
    Date lastUpdated

/* Transients */

/* Relations */
    static hasMany = [projects: Project]

/* Constraints */
    static constraints = {
        username(unique: true)
        password(blank: false)
    }

/* Mappings */
    static mapping = {
    }

/* Hooks */
    def beforeInsert = {
        password = password.encodeAsMD5()
    }

/* Named queries */

/* Transient Methods */

/* Methods */

/* Static Methods */

    public static User getUser(UserLoginCO userLoginCO) {
        User user
        if (userLoginCO.validate()) {
            String encryptedPassword = userLoginCO.password.encodeAsMD5()
            user = User.findByUsernameAndPassword(userLoginCO.username, encryptedPassword)
        }
        return user
    }

    public static User getLoggedInUser() {
        return (SessionUtil.userId ? User.get(SessionUtil.userId) : null)
    }

    public static void setLoggedInUser(Long id) {
        SessionUtil.setUserId(id)
    }
}

