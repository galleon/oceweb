package com.eads.threedviewer.co

import groovy.transform.ToString
import org.codehaus.groovy.grails.validation.Validateable
import org.jcae.opencascade.jni.BRepPrimAPI_MakeBox
import org.jcae.opencascade.jni.TopoDS_Shape
import org.springframework.web.multipart.MultipartFile
import com.eads.threedviewer.util.ShapeUtil

@Validateable
@ToString(includeNames = true, includeFields = true, excludes = 'metaClass,errors', includeSuper = true)
class FileShapeCO extends ShapeCO {
    MultipartFile file

    static constraints = {
        file(nullable: false, blank: false, minSize: 1)
    }

    @Override
    TopoDS_Shape getShape() {
        return ShapeUtil.getShape(content)
    }

    byte[] getContent() {
        return file.bytes
    }
}