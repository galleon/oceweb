package com.eads.threedviewer.co

import com.eads.threedviewer.util.ShapeUtil
import groovy.transform.ToString
import org.codehaus.groovy.grails.validation.Validateable
import org.jcae.opencascade.jni.TopoDS_Shape
import org.springframework.web.multipart.MultipartFile
import com.eads.threedviewer.CADObject

@Validateable
@ToString(includeNames = true, includeFields = true, excludes = 'metaClass,errors', includeSuper = true)
class FileShapeCO extends ShapeCO {
    MultipartFile file

    static constraints = {
        file(nullable: true, validator: {var, obj ->
            boolean isValid = true
            boolean isBrep = var?.originalFilename?.toLowerCase()?.endsWith(".brep")
            if (!obj.id && !isBrep) {
                isValid = false
            }
            else if (obj.id && var?.empty && !isBrep) {
                isValid = false
            }
            if (!isValid) {
                return ["content.type.not.supported", 'brep']
            }
        })
    }

    @Override
    TopoDS_Shape getShape() {
        return ShapeUtil.getShape(content)
    }

    byte[] getContent() {
        CADObject cadObject = super.findOrCreateCADObject()
        byte[] content = (id && !file) ? cadObject?.content : file?.bytes
        return content
    }
}