package com.truward.service.core.metrics;

/**
 * Represents default service metrics names.
 *
 * @author Alexander Shabanov
 */
public final class ServiceMetricsNames {
  private ServiceMetricsNames() {
  }

  /**
   * Parameter list.
   * Metrics entry name, associated with a list of string, each string is a stringified
   * parameter representation or null.
   */
  public static final String PARAMETERS = "parameters";

  /**
   * Global operation tracking ID.
   * Metrics entry name, associated with a stringified operation tracking id.
   * @see {@link com.truward.service.core.tracking.TrackingIdReader}
   */
  public static final String TRACKING_ID = "trackingId";
}
