package com.truward.service.core;

import com.truward.metrics.Metrics;
import org.slf4j.Logger;
import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.util.Assert;

import javax.annotation.Nonnull;
import java.util.concurrent.*;

public class AbstractService {

  protected final Logger log;

  protected final ServiceSettings serviceSettings;

  public AbstractService(@Nonnull ServiceSettings serviceSettings) {
    Assert.notNull(serviceSettings, "serviceSettings can't be null");

    this.serviceSettings = serviceSettings;
    this.log = this.serviceSettings.getLogger(getClass());
  }

  @Nonnull protected <T> Future<T> submit(@Nonnull Callable<T> callable) {
    return getTaskExecutor().submit(callable);
  }

  @Nonnull
  protected AsyncTaskExecutor getTaskExecutor() {
    return serviceSettings.getTaskExecutor();
  }

  protected <T> T get(@Nonnull Future<T> future) {
    try {
      return future.get(serviceSettings.getFutureGetTimeoutMillis(), TimeUnit.MILLISECONDS);
    } catch (InterruptedException | TimeoutException e) {
      throw serviceSettings.dispatchThrowable(e);
    } catch (ExecutionException e) {
      throw serviceSettings.dispatchThrowable(e.getCause());
    }
  }

  @Nonnull
  protected Metrics getTopLevelMetrics() {
    return serviceSettings.getServiceMetricsAware().getTopLevelMetrics();
  }
}
