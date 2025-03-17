package com.cnhis.dorislineage.demo.annotation;

import org.springframework.stereotype.Component;

import java.lang.annotation.*;

/**
 * SourceType
 */
@Component
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface SourceHandler {
    String value();
}
