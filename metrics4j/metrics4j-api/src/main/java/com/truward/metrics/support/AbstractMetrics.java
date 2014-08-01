package com.truward.metrics.support;

import com.truward.metrics.Metrics;

import javax.annotation.Nonnull;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Alexander Shabanov
 */
public abstract class AbstractMetrics implements Metrics {
  protected Map<String, Object> properties;

  protected AbstractMetrics(@Nonnull Map<String, Object> properties) {
    this.properties = properties;
  }

  protected AbstractMetrics() {
    this(new HashMap<String, Object>(20));
  }

  @Override public final void put(@Nonnull String name, boolean value) {
    this.properties.put(name, check(name, value));
  }

  @Override public final void put(@Nonnull String name, char value) {
    this.properties.put(name, check(name, value));
  }

  @Override public final void put(@Nonnull String name, int value) {
    this.properties.put(name, check(name, value));
  }

  @Override public final void put(@Nonnull String name, float value) {
    this.properties.put(name, check(name, value));
  }

  @Override public final void put(@Nonnull String name, double value) {
    this.properties.put(name, check(name, value));
  }

  @Override public final void put(@Nonnull String name, long value) {
    this.properties.put(name, check(name, value));
  }

  @Override public final void put(@Nonnull String name, @Nonnull CharSequence value) {
    this.properties.put(name, check(name, value));
  }

  @Override public final <T> void put(@Nonnull String name, @Nonnull Collection<T> value) {
    this.properties.put(name, check(name, value));
  }

  @Override public final <K, V> void put(@Nonnull String name, @Nonnull Map<K, V> value) {
    this.properties.put(name, check(name, value));
  }

  //
  // Private
  //

  private <T> T check(String name, T value) {
    if (name == null) {
      throw new IllegalArgumentException("name can't be null");
    }

    if (properties == null) {
      throw new IllegalStateException("Metric object is not writable, it has been closed");
    }

    return value;
  }
}
