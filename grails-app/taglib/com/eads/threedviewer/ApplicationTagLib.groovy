package com.eads.threedviewer

class ApplicationTagLib {

    static namespace = "td"

    def textField = {attrs ->
        String label = attrs['label']
        String name = attrs['name']
        String id = attrs['id'] ?: name
        def value = attrs['value'] ?: ''
        out << render(template: '/shared/textField', model: [label: label, name: name, id: id, value: value])
    }
}
