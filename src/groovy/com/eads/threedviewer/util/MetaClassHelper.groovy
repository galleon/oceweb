package com.eads.threedviewer.util

import javax.servlet.http.HttpServletRequest

class MetaClassHelper {
    public static void enrichClasses() {
        injectIsXhr()
    }

    public static void injectIsXhr() {
        HttpServletRequest.metaClass.isXhr = {->
            'XMLHttpRequest' == delegate.getHeader('X-Requested-With')
        }
    }

}
