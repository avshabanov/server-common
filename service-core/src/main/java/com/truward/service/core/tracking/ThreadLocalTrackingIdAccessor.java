package com.truward.service.core.tracking;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * @author Alexander Shabanov
 */
public class ThreadLocalTrackingIdAccessor implements TrackingIdReader {
  private final ThreadLocal<String> threadLocalTrackingId = new ThreadLocal<>();

  public void setTrackingId(@Nullable String trackingId) {
    threadLocalTrackingId.set(trackingId);
  }

  @Nonnull
  @Override
  public String getTrackingId() {
    final String result = threadLocalTrackingId.get();
    return result != null ? result : UNKNOWN_TRACKING_ID;
  }
}
