package com.truward.service.core.metrics.aspect;

import com.truward.metrics.Metrics;
import com.truward.metrics.MetricsCreator;
import com.truward.metrics.PredefinedMetricNames;
import com.truward.metrics.time.TimeService;
import com.truward.service.core.metrics.ServiceMetricsNames;
import com.truward.service.core.metrics.annotation.LogMetrics;
import com.truward.service.core.metrics.sink.ServiceMetricsSink;
import com.truward.service.core.tracking.TrackingIdReader;
import org.aspectj.lang.ProceedingJoinPoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

/**
 * An aspect, that processes {@link com.truward.service.core.metrics.annotation.LogMetrics} annotations.
 *
 * @author Alexander Shabanov
 */
public class MetricsLoggerAspect {
  private final Logger log = LoggerFactory.getLogger(getClass());

  private final MetricsCreator metricsCreator;
  private final ServiceMetricsSink serviceMetricsSink;
  private final TimeService timeService;
  private final TrackingIdReader trackingIdReader;

  private final boolean defaultIncludeParameters;
  private final boolean defaultLogException;

  public MetricsLoggerAspect(@Nonnull MetricsCreator metricsCreator,
                             @Nonnull ServiceMetricsSink serviceMetricsSink,
                             @Nonnull TimeService timeService,
                             @Nonnull TrackingIdReader trackingIdReader) {
    Assert.notNull(metricsCreator, "metricsCreator can't be null");
    Assert.notNull(serviceMetricsSink, "serviceMetricsSink can't be null");
    Assert.notNull(timeService, "timeService can't be null");
    Assert.notNull(trackingIdReader, "trackingIdReader can't be null");

    this.metricsCreator = metricsCreator;
    this.serviceMetricsSink = serviceMetricsSink;
    this.timeService = timeService;
    this.trackingIdReader = trackingIdReader;

    this.defaultIncludeParameters = false;
    this.defaultLogException = false;
  }

  @SuppressWarnings("UnusedDeclaration")
  public final Object around(ProceedingJoinPoint joinPoint, LogMetrics logMetrics) throws Throwable {
    assert joinPoint != null && logMetrics != null;

    final boolean measureTime = logMetrics.measureTime();
    final Metrics prevMetrics = serviceMetricsSink.getTopLevelMetrics();

    try (final Metrics metrics = metricsCreator.create()) {
      serviceMetricsSink.setTopLevelMetrics(metrics);

      // record metrics origin
      String originName = logMetrics.value();
      if (originName.length() == 0) {
        originName = joinPoint.getSignature().toShortString(); // Fallback to the method signature
      }
      metrics.put(PredefinedMetricNames.ORIGIN, originName);

      // record tracking ID
      final String trackingId = trackingIdReader.getTrackingId();
      metrics.put(ServiceMetricsNames.TRACKING_ID, trackingId);

      boolean succeeded = true;
      long startTime = 0L;
      try {
        if (measureTime) {
          startTime = timeService.now();
        }

        // call method
        return joinPoint.proceed();
      } catch (Throwable t) {
        succeeded = false;

        if (isIncluded(logMetrics.logException(), defaultLogException)) {
          log.error("Service Error: origin={} trackingId={}", originName, trackingId, t);
        }

        throw t; // rethrow exception
      } finally {
        // record metrics time
        if (measureTime) {
          final long delta = timeService.now() - startTime;
          metrics.put(PredefinedMetricNames.START_TIME, startTime);
          metrics.put(PredefinedMetricNames.TIME_DELTA, delta);
        }

        // record boolean 'succeeded' flag
        metrics.put(PredefinedMetricNames.SUCCEEDED, succeeded);

        // record parameters, if needed
        if (isIncluded(logMetrics.includeParameters(), defaultIncludeParameters)) {
          includeParameters(metrics, joinPoint);
        }
      }
    } finally {
      // set previous top-level metrics object
      serviceMetricsSink.setTopLevelMetrics(prevMetrics);
    }
  }

  //
  // Private
  //

  private boolean isIncluded(LogMetrics.InclusionMode inclusionMode, boolean defaultValue) {
    switch (inclusionMode) {
      case YES:
        return true;

      case NO:
        return false;

      case DEFAULT:
        return defaultValue;

      default:
        log.warn("Unknown inclusion mode: {}", defaultValue);
        return defaultIncludeParameters;
    }
  }

  private static void includeParameters(@Nonnull Metrics metrics,
                                        @Nonnull ProceedingJoinPoint joinPoint) {
    final Object[] args = joinPoint.getArgs();
    final List<String> readableParameters = new ArrayList<>(args.length);
    for (final Object arg : args) {
      readableParameters.add(String.valueOf(arg));
    }

    metrics.put(ServiceMetricsNames.PARAMETERS, readableParameters);
  }
}
