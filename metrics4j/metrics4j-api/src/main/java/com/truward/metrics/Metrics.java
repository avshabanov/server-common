package com.truward.metrics;

import javax.annotation.Nonnull;
import java.util.Collection;
import java.util.Map;

/**
 * Represents an entry in the metrics log.
 *
 * @author Alexander Shabanov
 */
public interface Metrics {

  void put(@Nonnull String name, boolean value);

  void put(@Nonnull String name, char value);

  void put(@Nonnull String name, int value);

  void put(@Nonnull String name, float value);

  void put(@Nonnull String name, double value);

  void put(@Nonnull String name, long value);

  void put(@Nonnull String name, @Nonnull CharSequence value);

  <T> void put(@Nonnull String name, @Nonnull Collection<T> value);

  <K, V> void put(@Nonnull String name, @Nonnull Map<K, V> value);

  void putCounter(@Nonnull String name, int count);

  void write();
}
