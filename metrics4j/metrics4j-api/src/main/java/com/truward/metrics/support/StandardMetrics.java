package com.truward.metrics.support;

import com.truward.metrics.dumper.MapDumper;

import javax.annotation.Nonnull;
import java.util.Map;

/**
 * @author Alexander Shabanov
 */
public class StandardMetrics extends AbstractMetrics {
  private MapDumper mapDumper;

  public StandardMetrics(@Nonnull Map<String, Object> properties, @Nonnull MapDumper mapDumper) {
    super(properties);
    this.mapDumper = mapDumper;
  }

  public StandardMetrics(@Nonnull MapDumper mapDumper) {
    super();
    this.mapDumper = mapDumper;
  }

  @Override public void close() {
    final MapDumper dumper = mapDumper;
    if (dumper == null) {
      throw new IllegalStateException("Metrics instance has been already closed.");
    }
    mapDumper = null;
    dumper.write(properties);
  }
}
