package com.eads.threedviewer.util

import javax.servlet.ServletContext
import org.codehaus.groovy.grails.commons.GrailsApplication
import org.springframework.context.ApplicationContext
import org.springframework.context.ApplicationContextAware

@Singleton
class ApplicationContextHolder implements ApplicationContextAware {

    private ApplicationContext ctx

    void setApplicationContext(ApplicationContext applicationContext) {
        ctx = applicationContext
    }

    static ApplicationContext getApplicationContext() {
        getInstance().ctx
    }

    static Object getBean(String name) {
        getApplicationContext().getBean(name)
    }

    static GrailsApplication getGrailsApplication() {
        getBean('grailsApplication') as GrailsApplication
    }

    static ConfigObject getConfig() {
        getGrailsApplication().config
    }

    static ServletContext getServletContext() {
        getBean('servletContext') as ServletContext
    }
}