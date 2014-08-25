package com.truward.service.core.metrics.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Annotation, that indicates that metrics should be recorded for this method.
 * @see com.truward.metrics.Metrics
 *
 * @author Alexander Shabanov
 */
@Target({TYPE, METHOD})
@Retention(RUNTIME)
public @interface LogMetrics {

  /**
   * Specifies an origin name.
   * The recommended value is an interface name contatenated with the method name,
   * e.g. <code>UserService.register</code> or <code>BlogService.addRecord</code>
   * @see {@link com.truward.metrics.PredefinedMetricNames#ORIGIN}
   *
   * @return Origin name as string.
   */
  String value() default "";

  boolean measureTime() default true;

  InclusionMode includeParameters() default InclusionMode.DEFAULT;

  InclusionMode logException() default InclusionMode.DEFAULT;

  enum InclusionMode {
    DEFAULT,

    YES,

    NO
  }
}
