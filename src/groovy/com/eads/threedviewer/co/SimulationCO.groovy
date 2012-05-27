package com.eads.threedviewer.co

import com.eads.threedviewer.enums.PolarisationType
import com.eads.threedviewer.enums.ProcessingType
import grails.validation.Validateable
import groovy.transform.ToString

@Validateable
@ToString(includeNames = true, includeFields = true, excludes = 'metaClass,errors')
class SimulationCO {
    Long id
    Float frequency
    Float theta
    Float phi
    PolarisationType polarisationType
    ProcessingType processingType
    List<SimulationDomainCO> domains = []

    static constraints = {
        frequency(nullable: false)
        theta(nullable: false)
        phi(nullable: false)
        polarisationType(nullable: false)
        processingType(nullable: false)
    }

}
