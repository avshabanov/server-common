package com.truward.metrics.json;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ContainerNode;
import com.truward.metrics.Metrics;
import com.truward.metrics.PredefinedMetricNames;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

/**
 * Multithread integration tests for {@link com.truward.metrics.json.JsonLogMetricsCreator}.
 *
 * @author Alexander Shabanov
 */
@Ignore
public final class ConcurrentJsonLogCreatorTest {
  private JsonLogMetricsCreator metricsCreator;
  private ThreadPoolExecutor executor;
  private File file;
  private final ObjectMapper mapper = new ObjectMapper();

  @Before
  public void init() throws IOException {
    file = File.createTempFile("metrics4j", "concurrentTest");
    metricsCreator = new JsonLogMetricsCreator(file);
    executor = new ThreadPoolExecutor(10, 100, 1L, TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(100));
  }

  @After
  public void close() throws IOException {
    metricsCreator.close();
    executor.shutdown();
  }

  @Test
  public void shouldWriteMultipleValues() throws Exception {
    // Given:
    final int entryCount = 100;
    final List<Future<Void>> tasks = new ArrayList<>(entryCount);

    // When:
    for (int i = 0; i < entryCount; ++i) {
      tasks.add(executor.submit(new Callable<Void>() {
        @Override public Void call() throws Exception {
          try (final Metrics metrics = metricsCreator.create()) {
            metrics.put(PredefinedMetricNames.ORIGIN, "test");
            metrics.put(PredefinedMetricNames.START_TIME, System.currentTimeMillis());
            metrics.put(PredefinedMetricNames.TIME_DELTA, 10L);
            metrics.put(PredefinedMetricNames.SUCCEEDED, true);
          }

          return null;
        }
      }));
    }

    // Then:
    for (int i = 0; i < entryCount; ++i) {
      assertNull(tasks.get(i).get());
    }

    try (final InputStream inputStream = new BufferedInputStream(new FileInputStream(file))) {
      for (int i = 0; i < entryCount; ++i) {
        final ContainerNode containerNode = mapper.readValue(inputStream, ContainerNode.class);
        assertNotNull(containerNode);
      }
    }
  }
}
