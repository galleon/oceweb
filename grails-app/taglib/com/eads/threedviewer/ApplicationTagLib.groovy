package com.eads.threedviewer

import org.codehaus.groovy.grails.web.context.ServletContextHolder as SCH

class ApplicationTagLib {

    static namespace = "td"

    def textField = {attrs ->
        String label = attrs['label']
        String name = attrs['name']
        String id = attrs['id'] ?: name
        boolean isRequired = !(attrs['required'] == 'false')
        def value = attrs['value'] ?: (attrs['default'] ?: '')
        out << render(template: '/shared/textField', model: [label: label, name: name, id: id, value: value, isRequired: isRequired])
    }

    def versionNumber = {
        String fileName = SCH.servletContext.getRealPath("/") + grailsApplication.config.versionFileName.toString()
        File file = new File(fileName)
        if (file.exists()) {
            file.text.eachLine {line ->
                if (line.startsWith("commit")) {
                    out << line.tokenize(" ").last()
                }
            }
        }
    }
}
