package com.truward.service.core.tracking;

import javax.annotation.Nonnull;

/**
 * An access interface to the global tracking ID.
 * This ID is used to differentiate operations for handling the particular service call from each other.
 *
 * @author Alexander Shabanov
 */
public interface TrackingIdReader {

  /**
   * Special value for unknown tracking ID.
   */
  String UNKNOWN_TRACKING_ID = "?";

  /**
   * @return Global tracking identifier.
   */
  @Nonnull
  String getTrackingId();
}
