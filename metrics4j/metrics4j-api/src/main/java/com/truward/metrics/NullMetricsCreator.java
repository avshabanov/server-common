package com.truward.metrics;

import javax.annotation.Nonnull;
import java.util.Collection;
import java.util.Map;

/**
 * @author Alexander Shabanov
 */
public final class NullMetricsCreator implements MetricsCreator {

  private static final NullMetrics NULL_METRICS_INSTANCE = new NullMetrics();
  private static final NullMetricsCreator INSTANCE = new NullMetricsCreator();


  @Nonnull
  public static NullMetricsCreator getInstance() {
    return INSTANCE;
  }

  @Nonnull @Override public Metrics create() {
    return NULL_METRICS_INSTANCE;
  }

  //
  // Private
  //

  private static final class NullMetrics implements Metrics {

    public NullMetrics() {
    }

    @Override public void put(@Nonnull String name, boolean value) {
      // do nothing
    }

    @Override public void put(@Nonnull String name, char value) {
      // do nothing
    }

    @Override public void put(@Nonnull String name, int value) {
      // do nothing
    }

    @Override public void put(@Nonnull String name, float value) {
      // do nothing
    }

    @Override public void put(@Nonnull String name, double value) {
      // do nothing
    }

    @Override public void put(@Nonnull String name, long value) {
      // do nothing
    }

    @Override public void put(@Nonnull String name, @Nonnull CharSequence value) {
      // do nothing
    }

    @Override public <T> void put(@Nonnull String name, @Nonnull Collection<T> value) {
      // do nothing
    }

    @Override public <K, V> void put(@Nonnull String name, @Nonnull Map<K, V> value) {
      // do nothing
    }

    @Override public void putCounter(@Nonnull String name, int count) {
      // do nothing
    }

    @Override public void write() {
      // do nothing
    }
  }
}
