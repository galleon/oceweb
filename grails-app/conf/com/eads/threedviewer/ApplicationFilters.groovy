package com.eads.threedviewer

class ApplicationFilters {

    def filters = {
        Boolean accessDenied = false

        all(controller: '*', action: '*') {
            before = {
                log.info "GrailsAccessLog:${params.findAll {!it.key.toString().toLowerCase().contains('password')}}"
            }
        }

        consoleImports(controller: 'console', action: '*') {
            before = {
                String importStatements = """import com.eads.threedviewer.*
import com.eads.threedviewer.co.*
import com.eads.threedviewer.vo.*
import com.eads.threedviewer.util.*
import com.eads.threedviewer.enum.*"""
                session['_grails_console_last_code_'] = session['_grails_console_last_code_'] ?: importStatements
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
