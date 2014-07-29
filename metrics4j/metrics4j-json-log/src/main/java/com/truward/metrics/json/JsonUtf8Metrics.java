package com.truward.metrics.json;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.io.IOContext;
import com.fasterxml.jackson.core.json.UTF8JsonGenerator;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;

/**
 * @author Alexander Shabanov
 */
public final class JsonUtf8Metrics extends AbstractMetrics {
  private OutputStream outputStream;

  @Override public void write() {
    final OutputStream os = outputStream;
    if (os == null) {
      throw new IllegalStateException("Metrics instance is no longer writable.");
    }

    outputStream = null;


  }

  //
  // Private
  //

  private static void writeProperties(@Nonnull OutputStream os,
                                      @Nonnull Map<String, Object> properties) throws IOException {
    UTF8JsonGenerator jg;
  }
}
