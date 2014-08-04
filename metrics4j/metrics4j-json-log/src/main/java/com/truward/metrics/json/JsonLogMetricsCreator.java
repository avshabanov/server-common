package com.truward.metrics.json;

import com.truward.metrics.Metrics;
import com.truward.metrics.MetricsCreator;
import com.truward.metrics.dumper.MapDumper;
import com.truward.metrics.json.dumper.JacksonMapDumper;
import com.truward.metrics.support.StandardMetrics;

import javax.annotation.Nonnull;
import java.io.*;

/**
 * Metrics creator that dumps metrics as UTF-8 encoded JSON into the given output stream.
 *
 * @author Alexander Shabanov
 */
public class JsonLogMetricsCreator implements MetricsCreator, Closeable {

  private MapDumper mapDumper;
  private OutputStream outputStream;

  public JsonLogMetricsCreator(@Nonnull OutputStream outputStream) {
    this.outputStream = outputStream;
    this.mapDumper = createMapDumper(outputStream);
  }

  public JsonLogMetricsCreator(@Nonnull File file) throws FileNotFoundException {
    this(new BufferedOutputStream(new FileOutputStream(file, true), 4096));
  }

  @Nonnull
  @Override
  public Metrics create() {
    if (mapDumper == null) {
      throw new IllegalStateException("Can't create metric instance: output stream has been closed");
    }
    return new StandardMetrics(mapDumper);
  }

  @Override
  public void close() throws IOException {
    mapDumper = null;

    if (outputStream != null) {
      outputStream.close();
      outputStream = null;
    }
  }

  // Visible For Tests
  @Nonnull
  protected MapDumper createMapDumper(@Nonnull OutputStream outputStream) {
    return new JacksonMapDumper(outputStream);
  }
}
