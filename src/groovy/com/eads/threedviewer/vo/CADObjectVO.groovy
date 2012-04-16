package com.eads.threedviewer.vo

import com.eads.threedviewer.enums.ShapeType

class CADObjectVO {
    Long id
    String name
    ShapeType type
    ShapeType parentType
    List<CADObjectVO> subCadObjects
}
