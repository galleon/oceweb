package com.eads.threedviewer.util

import java.lang.annotation.ElementType
import java.lang.annotation.Retention
import java.lang.annotation.RetentionPolicy
import java.lang.annotation.Target

@Target([ElementType.FIELD, ElementType.TYPE])
@Retention(RetentionPolicy.RUNTIME)
@interface RequiredSession {
    String[] exclude() default []

    String[] fields() default ["userId"]

}