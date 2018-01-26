package com.commons.annotation;

import java.lang.annotation.*;

/**
 *
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.METHOD })
public @interface Version {
	
	String version() default "1.0";
}
