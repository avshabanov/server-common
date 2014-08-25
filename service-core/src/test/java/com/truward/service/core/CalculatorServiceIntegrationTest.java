package com.truward.service.core;

import com.truward.metrics.Metrics;
import com.truward.metrics.json.reader.StandardJsonMetricsReader;
import com.truward.metrics.reader.MetricsReader;
import com.truward.service.core.metrics.annotation.LogMetrics;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.stereotype.Service;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Nonnull;
import javax.annotation.Resource;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Map;

import static org.junit.Assert.*;

/**
 * @author Alexander Shabanov
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/spring/CalculatorServiceIntegrationTest-context.xml" })
public class CalculatorServiceIntegrationTest {

  @Resource CalculatorService calculatorService;

  @Resource ByteArrayOutputStream testOutputStream;

  @Test
  public void shouldWrapCalculatorServiceIntoAnAspect() {
    assertFalse(calculatorService instanceof CalculatorServiceImpl);
  }

  @Test
  public void shouldRecordMetrics() throws IOException {
    // Given:
    int a = 1, b = 2;

    // When:
    final int result = calculatorService.add(a, b);

    // Then:
    assertEquals(a + b, result);

    try (MetricsReader r = new StandardJsonMetricsReader(new ByteArrayInputStream(testOutputStream.toByteArray()))) {
      final Map<String, ?> metrics = r.readNext();
      assertNotNull("metrics shouldn't be null", metrics);
      assertNull("there should be only one metrics entry", r.readNext());

      assertEquals(a, metrics.get("a"));
    }
  }

  //
  // Test Service
  //

  public interface CalculatorService {
    int add(int a, int b);
  }

  @Service
  public static final class CalculatorServiceImpl extends AbstractService implements CalculatorService {

    public CalculatorServiceImpl(@Nonnull ServiceSettings serviceSettings) {
      super(serviceSettings);
    }

    @Override
    @LogMetrics("CalculatorService.add")
    public int add(int a, int b) {
      // record arguments
      final Metrics metrics = getTopLevelMetrics();
      metrics.put("a", a);
      metrics.put("b", b);

      return a + b;
    }
  }
}
