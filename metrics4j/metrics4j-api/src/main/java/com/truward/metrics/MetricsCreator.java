package com.truward.metrics;

import javax.annotation.Nonnull;

/**
 * A MetricsCreator object is used to create instances of {@link com.truward.metrics.Metrics} class.
 * MetricsCreator also encapsulates a way which is used to store and represent the newly created metrics.
 *
 * @see com.truward.metrics.Metrics
 *
 * @author Alexander Shabanov
 */
public interface MetricsCreator {

  @Nonnull
  Metrics create();
}
