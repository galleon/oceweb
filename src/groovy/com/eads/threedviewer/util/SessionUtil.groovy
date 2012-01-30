package com.eads.threedviewer.util

import org.springframework.web.context.request.RequestContextHolder
import groovy.util.logging.Log

@Log
class SessionUtil {

    public static def getSession() {
        def session
        try {
            session = RequestContextHolder?.currentRequestAttributes()?.session
        } catch (Exception e) {
            e.printStackTrace()
        }
        return session
    }

    public static void set(key, value) {
        log.info "Setting ${value} for key ${key}"
        session[key] = value
    }

    public static def get(key) {
        return session[key]
    }

    public static void clear(key) {
        set(key, null)
    }

    public static void setUserId(Long value) {
        set('userId', value)
    }

    public static Long getUserId() {
        get('userId') ? get('userId').toLong() : null
    }
}