package com.truward.metrics.dumper;

import javax.annotation.Nonnull;
import java.util.Map;

/**
 * Represents an abstraction over certain dumper that takes care about writing certain map into some storage.
 *
 *
 * @author Alexander Shabanov
 */
public interface MapDumper {

  /**
   * Writes a map into the associated storage.
   * <p>
   * Each value in the given map should be either of primitive type, or BigDecimal or String-to-Object map or
   * list of objects.
   * Each object in the aforementioned collections should be of any types described above.
   * </p>
   * <p>
   * An exception thrown will be suppressed and logged. This method guarantees to not to throw any exception related
   * to unsuccessful I/O operation.
   * </p>
   * <p>
   * Each metrics object should invoke this method only once and then clear reference to it.
   * </p>
   *
   * @param properties A map that should be written into the corresponding storage.
   */
  void write(@Nonnull Map<String, Object> properties);
}
