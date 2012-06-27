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
    String name
    static constraints = {
        epsilon(nullable: false, blank: false)
        sigma(nullable: false, blank: false)
        mu(nullable: false, blank: false)
        sigma1(nullable: false, blank: false)
          }
}
