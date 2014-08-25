package com.truward.service.core;

import com.truward.service.core.exception.BaseServiceException;
import com.truward.service.core.exception.GenericServiceException;
import com.truward.service.core.metrics.sink.ServiceMetricsAware;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.util.Assert;

import javax.annotation.Nonnull;

/**
 * Base settings for service.
 *
 * @author Alexander Shabanov
 */
public class ServiceSettings {
  private final AsyncTaskExecutor taskExecutor;
  private final ServiceMetricsAware serviceMetricsAware;

  public ServiceSettings(@Nonnull AsyncTaskExecutor taskExecutor,
                         @Nonnull ServiceMetricsAware serviceMetricsAware) {
    Assert.notNull(taskExecutor, "taskExecutor can't be null");
    Assert.notNull(serviceMetricsAware, "serviceMetricsAware can't be null");
    this.taskExecutor = taskExecutor;
    this.serviceMetricsAware = serviceMetricsAware;
  }

  @Nonnull
  public AsyncTaskExecutor getTaskExecutor() {
    return taskExecutor;
  }

  public long getFutureGetTimeoutMillis() {
    return 10000L;
  }

  @Nonnull
  public ServiceMetricsAware getServiceMetricsAware() {
    return serviceMetricsAware;
  }

  @Nonnull
  public Logger getLogger(@Nonnull Class<?> serviceClass) {
    return LoggerFactory.getLogger(serviceClass);
  }

  @Nonnull
  public final RuntimeException dispatchThrowable(@Nonnull Throwable cause) {
    if (cause instanceof Error) {
      throw ((Error) cause); // rethrow cause as is for errors
    }

    if (cause instanceof Exception) {
      // do not rethrow base service exceptions
      if (cause instanceof BaseServiceException) {
        return ((BaseServiceException) cause);
      }

      return dispatchNonServiceException((Exception) cause);
    }

    // shouldn't happen: only errors and exceptions could be thrown
    return new GenericServiceException("Undispatched exception", cause);
  }

  @Nonnull
  protected RuntimeException dispatchNonServiceException(@Nonnull Exception cause) {
    return new GenericServiceException(cause);
  }
}
