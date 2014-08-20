package com.truward.service.core;

import com.truward.service.core.exception.GenericServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.task.AsyncTaskExecutor;

import javax.annotation.Nonnull;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class AbstractService {

  protected final Logger log = LoggerFactory.getLogger(getClass());

  protected AsyncTaskExecutor taskExecutor;

  @Nonnull protected <T> Future<T> submit(@Nonnull Callable<T> callable) {
    return taskExecutor.submit(callable);
  }

  protected <T> T get(@Nonnull Future<T> future) {
    try {
      return future.get();
    } catch (InterruptedException e) {
      throw new GenericServiceException(e);
    } catch (ExecutionException e) {
      throw dispatchException(e);
    }
  }

  @Nonnull protected RuntimeException dispatchException(@Nonnull Exception cause) {
    return new GenericServiceException(cause);
  }
}
