package com.eads.threedviewer

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
        File file = new File("versionInfo.txt")
        if (file.exists()) {
            file.text.eachLine {line ->
                if (line.startsWith("commit")) {
                    out << line.tokenize(" ").last()
                }
            }
        }
    }
}
