package com.eads.threedviewer.util

import javax.servlet.http.HttpServletRequest

class MetaClassHelper {
    public static void enrichClasses() {
        injectS()
        injectIsXhr()
    }

    public static void injectS() {
        Object.metaClass.s = {
            def o = delegate.save(flush: true)
            if (!o) {
                delegate.errors.allErrors.each {
                    log.error it
                }
            }
            return o
        }

    }

    public static void injectIsXhr() {
        HttpServletRequest.metaClass.isXhr = {->
            'XMLHttpRequest' == delegate.getHeader('X-Requested-With')
        }
    }

}
