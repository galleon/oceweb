package com.eads.threedviewer

class CadObjectService {
    void deleteCADObject(Set<Long> ids) {
        List<CADObject> cadObjects = ids ? CADObject.getAll(ids.toList()) : []
        List childrenList = CADObject.findAllByParentInList(cadObjects)
        childrenList*.parent = null
        cadObjects*.delete()
    }
}
