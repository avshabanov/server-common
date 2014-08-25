package com.truward.service.core.metrics.sink;

import com.truward.metrics.Metrics;

import javax.annotation.Nonnull;

/**
 * @author Alexander Shabanov
 */
public interface ServiceMetricsAware {

  @Nonnull Metrics getTopLevelMetrics();
}
