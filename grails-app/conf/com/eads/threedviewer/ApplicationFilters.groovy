package com.eads.threedviewer

import com.eads.threedviewer.util.RequiredSession

class ApplicationFilters {

    def filters = {
        Boolean accessDenied = false

        all(controller: '*', action: '*') {
            before = {
                log.info "GrailsAccessLog:${params.findAll {!it.key.toString().toLowerCase().contains('password')}}"
            }
        }

        validateSession(controller: '*', action: '*') {
            before = {
                if (controllerName) {
                    def controllerClass = grailsApplication.controllerClasses.find {it.logicalPropertyName == controllerName}
                    def annotation = controllerClass.clazz.getAnnotation(RequiredSession)
                    String currentAction = actionName ?: controllerClass.defaultActionName
                    if (!annotation || currentAction in annotation.exclude()) {
                        def currentControllerClass =    applicationContext.getBean(controllerClass.fullName).class
                        def action = currentControllerClass.class.declaredFields.find { field -> field.name == currentAction }
                        annotation = action ? action.getAnnotation(RequiredSession) : null
                    }

                    accessDenied = annotation ? (annotation.fields().any {session[it] == null}) : false
                    if (accessDenied) {
                        flash.error = "You are not Logged in. Please login"
                    }
                }
            }

        }

        consoleImports(controller: 'console', action: '*') {
            before = {
                if (User.loggedInUser?.isAdmin) {
                    String importStatements = "import com.eads.threedviewer.*"
                    session['_grails_console_last_code_'] = session['_grails_console_last_code_'] ?: importStatements
                } else {
                    accessDenied = true
                }
            }
        }

        checkAccessDenied(controller: "*", action: '*') {
            before = {
                if (accessDenied) {
                    if (request.xhr) {
                        String text = "Session TimedOut url=" + grailsApplication.config.grails.serverURL
                        render(text: text, contentType: 'text/plain')
                    } else {
                        redirect(uri: "/")
                    }
                    return false
                }
            }
        }

    }

}
