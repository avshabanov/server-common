package com.truward.service.core.metrics.sink;

import com.truward.metrics.Metrics;
import com.truward.metrics.NullMetricsCreator;

import javax.annotation.Nonnull;

/**
 * @author Alexander Shabanov
 */
public final class ServiceMetricsSink implements ServiceMetricsAware {
  private final ThreadLocal<Metrics> topLevelMetrics = new ThreadLocal<>();

  public void setTopLevelMetrics(@Nonnull Metrics metrics) {
    this.topLevelMetrics.set(metrics);
  }

  @Nonnull
  @Override
  public Metrics getTopLevelMetrics() {
    final Metrics result = topLevelMetrics.get();
    if (result != null) {
      return result;
    }

    return NullMetricsCreator.NULL_METRICS;
  }
}
