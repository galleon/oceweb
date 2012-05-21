package com.eads.threedviewer.dto

import groovy.transform.ToString
import groovy.util.logging.Log

@Log
@ToString(includeNames = true, includeFields = true, excludes = 'metaClass')
class GroupDTO {
    List values = []
    String name
    Integer entityCount

}