package com.eads.threedviewer

import grails.converters.deep.JSON

class CadObjectService {
    Map deleteCADObject(Set<Long> ids, String message) {
        Map result = ['success': 'Deleted Successfully']
        List<CADObject> cadObjects = ids ? CADObject.getAll(ids.toList()) : []
        try {
            List childrenList = CADObject.findAllByParentInList(cadObjects)
            childrenList*.parent = null
            cadObjects*.delete()
        } catch (RuntimeException rte) {
            result = ['error': message]
        }
        return result
    }
}
