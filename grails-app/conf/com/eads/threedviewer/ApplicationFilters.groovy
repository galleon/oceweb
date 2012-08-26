package com.eads.threedviewer

import org.codehaus.groovy.grails.commons.ConfigurationHolder

class ApplicationFilters {

    def filters = {
        Boolean accessDenied = false

        all(controller: '*', action: '*') {
            before = {
                log.info "GrailsAccessLog:${params.findAll {!it.key.toString().toLowerCase().contains('password')}}"
                session.scale = session.scale ?: ConfigurationHolder.config.scale.size
            }
        }

        consoleImports(controller: 'console', action: '*') {
            before = {
                String importStatements = """import com.eads.threedviewer.*
import com.eads.threedviewer.util.*
import com.eads.threedviewer.co.*
import com.eads.threedviewer.dto.*
import com.eads.threedviewer.enums.*
"""
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
