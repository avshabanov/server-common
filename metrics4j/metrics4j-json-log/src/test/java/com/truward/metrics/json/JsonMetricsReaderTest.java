package com.truward.metrics.json;

import com.truward.metrics.Metrics;
import com.truward.metrics.PredefinedMetricNames;
import com.truward.metrics.json.reader.MetricsReader;
import com.truward.metrics.json.util.ObjectMapperMetricsReader;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

/**
 * Tests for {@link com.truward.metrics.json.reader.AbstractJsonMetricsReader}.
 *
 * @author Alexander Shabanov
 */
public final class JsonMetricsReaderTest {
  private ByteArrayOutputStream os;
  private JsonLogMetricsCreator metricsCreator;

  @Before
  public void init() {
    os = new ByteArrayOutputStream(1000);
    metricsCreator = new JsonLogMetricsCreator(os);
  }

  @Test
  public void shouldWriteAndReadMetrics() throws IOException {
    // Given:
    final int entriesCount = 100;
    for (int i = 0; i < entriesCount; ++i) {
      try (final Metrics metrics = metricsCreator.create()) {
        metrics.put(PredefinedMetricNames.ORIGIN, "test");
        metrics.put(PredefinedMetricNames.START_TIME, 1000);
        metrics.put(PredefinedMetricNames.SUCCEEDED, true);
        metrics.put("id", i);
      }
    }

    // When:
    metricsCreator.close();

    // Then:
    assertMetricsRead(entriesCount, 100, 100);
    assertMetricsRead(entriesCount, 10, 100);
    assertMetricsRead(entriesCount, 11, 107);
    assertMetricsRead(entriesCount, 9, 101);
    assertMetricsRead(entriesCount, 1, 100);
  }

  //
  // Private
  //

  private MetricsReader newMetricsReader(int initialBufferSize, int maxBufferSize) {
    return new ObjectMapperMetricsReader(new ByteArrayInputStream(os.toByteArray()), initialBufferSize, maxBufferSize);
  }

  private void assertMetricsRead(int expectedCount, int initialBufferSize, int maxBufferSize) throws IOException {
    try (final MetricsReader reader = newMetricsReader(initialBufferSize, maxBufferSize)) {
      for (int i = 0; i < expectedCount; ++i) {
        final Map<String, ?> metrics = reader.readNext();
        assertNotNull("Entry #" + i + " not found for initialBufferSize=" + initialBufferSize +
            ", maxBufferSize=" + maxBufferSize, metrics);

        assertEquals(4, metrics.size());
        assertEquals(i, metrics.get("id"));
        assertEquals("test", metrics.get(PredefinedMetricNames.ORIGIN));
        assertEquals(1000, metrics.get(PredefinedMetricNames.START_TIME));
        assertEquals(true, metrics.get(PredefinedMetricNames.SUCCEEDED));
      }

      assertNull("There should be no more metrics", reader.readNext());
    }
  }
}
