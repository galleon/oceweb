package com.eads.threedviewer.co

import grails.validation.Validateable
import groovy.transform.ToString

@Validateable
@ToString(includeNames = true, includeFields = true, excludes = 'metaClass,errors')
class SimulationDomainCO {
    Float epsilon
    Float sigma
    Float mu
    Float sigma1

}
