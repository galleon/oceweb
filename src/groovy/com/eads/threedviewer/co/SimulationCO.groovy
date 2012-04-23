package com.eads.threedviewer.co

import com.eads.threedviewer.CADMeshObject
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
    Float epsilon1
    Float epsilon2
    Float sigma1
    Float sigma2
    Float mu1
    Float mu2
    Float sigma_1
    Float sigma_2

    static constraints = {
        frequency(nullable: false)
        theta(nullable: false)
        phi(nullable: false)
        polarisationType(nullable: false)
        processingType(nullable: false)
    }

}
