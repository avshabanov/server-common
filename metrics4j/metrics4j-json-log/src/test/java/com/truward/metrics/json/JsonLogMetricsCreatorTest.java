package com.truward.metrics.json;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.truward.metrics.Metrics;
import com.truward.metrics.PredefinedMetricNames;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.Map;

import static org.junit.Assert.assertEquals;

/**
 * Tests for {@link com.truward.metrics.json.JsonLogMetricsCreator}.
 *
 * @author Alexander Shabanov
 */
public final class JsonLogMetricsCreatorTest {
  private ByteArrayOutputStream byteArrayOutputStream;
  private JsonLogMetricsCreator metricsCreator;
  private final ObjectMapper mapper = new ObjectMapper();
  private final TypeReference<Map<String, Object>> mapTypeReference = new TypeReference<Map<String, Object>>() {};

  @Before
  public void init() {
    byteArrayOutputStream = new ByteArrayOutputStream(1000);
    metricsCreator = new JsonLogMetricsCreator(byteArrayOutputStream);
  }

  @Test
  public void shouldDumpStandardValues() throws IOException {
    // Given:
    try (final Metrics metrics = metricsCreator.create()) {
      metrics.put(PredefinedMetricNames.ORIGIN, "testOrigin");
      metrics.put(PredefinedMetricNames.START_TIME, 1000L);
      metrics.put(PredefinedMetricNames.TIME_DELTA, 250L);
      metrics.put(PredefinedMetricNames.SUCCEEDED, true);
    }

    // When:
    metricsCreator.close();

    // Then:
    final Map<String, Object> map = mapper.readValue(byteArrayOutputStream.toByteArray(), mapTypeReference);
    assertEquals(4, map.size());
    assertEquals("testOrigin", map.get(PredefinedMetricNames.ORIGIN));
    assertEquals(true, map.get(PredefinedMetricNames.SUCCEEDED));
    assertEquals(1000, map.get(PredefinedMetricNames.START_TIME));
    assertEquals(250, map.get(PredefinedMetricNames.TIME_DELTA));
  }

  @Test
  public void shouldDumpNestedMap() throws IOException {
    // Given:
    try (final Metrics metrics = metricsCreator.create()) {
      metrics.put("nested", Collections.singletonMap("val", 1L));
    }

    // When:
    metricsCreator.close();

    // Then:
    final Map<String, Object> map = mapper.readValue(byteArrayOutputStream.toByteArray(), mapTypeReference);
    assertEquals(1, map.size());
    assertEquals(Collections.singletonMap("val", 1), map.get("nested"));
  }

  @Test
  public void shouldDumpArray() throws IOException {
    // Given:
    try (final Metrics metrics = metricsCreator.create()) {
      metrics.put("array", Collections.singletonList("val"));
    }

    // When:
    metricsCreator.close();

    // Then:
    final Map<String, Object> map = mapper.readValue(byteArrayOutputStream.toByteArray(), mapTypeReference);
    assertEquals(1, map.size());
    assertEquals(Collections.singletonList("val"), map.get("array"));
  }

  @Test
  @Ignore
  public void shouldDumpMultipleMetrics() throws IOException {
    // Given:
    try (final Metrics metrics = metricsCreator.create()) {
      metrics.put("val", "a");
    }
    try (final Metrics metrics = metricsCreator.create()) {
      metrics.put("val", "b");
    }

    // When:
    metricsCreator.close();

    // Then:
    final String s = new String(byteArrayOutputStream.toByteArray(), "UTF-8");
    try (final InputStream inputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray())) {
      final Map<String, Object> map1 = mapper.readValue(inputStream, mapTypeReference);
      assertEquals(1, map1.size());
      assertEquals("a", map1.get("val"));

      final Map<String, Object> map2 = mapper.readValue(inputStream, mapTypeReference);
      assertEquals(1, map1.size());
      assertEquals("b", map2.get("val"));
    }
  }
}
