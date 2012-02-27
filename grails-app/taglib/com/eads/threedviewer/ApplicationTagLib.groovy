package com.eads.threedviewer

class ApplicationTagLib {

    static namespace = "td"

    def textField = {attrs ->
        String label = attrs['label']
        String name = attrs['name']
        String id = attrs['id'] ?: name
        boolean isRequired = !(attrs['required'] == 'false')
        String required = isRequired ? "required='true'" : ""
        def value = attrs['value'] ?: (attrs['default'] ?: '')
        out << render(template: '/shared/textField', model: [label: label, name: name, id: id, value: value, required: required])
    }
}
