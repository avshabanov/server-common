package com.truward.metrics.json;

import com.truward.metrics.Metrics;

import javax.annotation.Nonnull;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Alexander Shabanov
 */
public abstract class AbstractMetrics implements Metrics {
  public static final String COUNTERS_FIELD_NAME = "counters";

  protected final Map<String, Object> properties;
  protected final Map<String, Integer> counters;

  protected AbstractMetrics(int propertiesCapacity, int countersCapacity) {
    this.properties = new HashMap<>(propertiesCapacity);
    this.counters = new HashMap<>(countersCapacity);
    put(COUNTERS_FIELD_NAME, counters);
  }

  protected AbstractMetrics() {
    this(20, 4);
  }

  @Override public final void put(@Nonnull String name, boolean value) {
    checkTopLevelName(name);
    this.properties.put(name, value);
  }

  @Override public final void put(@Nonnull String name, char value) {
    checkTopLevelName(name);
    this.properties.put(name, value);
  }

  @Override public final void put(@Nonnull String name, int value) {
    checkTopLevelName(name);
    this.properties.put(name, value);
  }

  @Override public final void put(@Nonnull String name, float value) {
    checkTopLevelName(name);
    this.properties.put(name, value);
  }

  @Override public final void put(@Nonnull String name, double value) {
    checkTopLevelName(name);
    this.properties.put(name, value);
  }

  @Override public final void put(@Nonnull String name, long value) {
    checkTopLevelName(name);
    this.properties.put(name, value);
  }

  @Override public final void put(@Nonnull String name, @Nonnull CharSequence value) {
    checkTopLevelName(name);
    this.properties.put(name, value);
  }

  @Override public final <T> void put(@Nonnull String name, @Nonnull Collection<T> value) {
    checkTopLevelName(name);
    this.properties.put(name, value);
  }

  @Override public final <K, V> void put(@Nonnull String name, @Nonnull Map<K, V> value) {
    checkTopLevelName(name);
    this.properties.put(name, value);
  }

  @Override public final void putCounter(@Nonnull String name, int count) {
    this.counters.put(name, count);
  }

  //
  // Private
  //

  private void checkTopLevelName(@Nonnull String name) {
    if (this.properties.containsKey(name)) {
      throw new IllegalStateException("Duplicate entry with name=" + name);
    }
  }
}
