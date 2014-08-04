package com.truward.metrics.json.util;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.truward.metrics.json.reader.AbstractJsonMetricsReader;
import org.junit.Ignore;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

/**
 * Child of {@link com.truward.metrics.json.reader.AbstractJsonMetricsReader} that
 * uses {@link com.fasterxml.jackson.databind.ObjectMapper} to read a json object from the input stream.
 *
 * @author Alexander Shabanov
 */
@Ignore
public final class ObjectMapperMetricsReader extends AbstractJsonMetricsReader {
  private final ObjectMapper mapper = new ObjectMapper();

  public ObjectMapperMetricsReader(@Nonnull InputStream inputStream, int initialBufferSize, int maxBufferSize) {
    super(inputStream, initialBufferSize, maxBufferSize);
  }

  public ObjectMapperMetricsReader(@Nonnull InputStream inputStream) {
    super(inputStream);
  }

  @Nonnull
  @Override
  protected Map<String, Object> parseJson(@Nonnull byte[] arr, int startPos, int len) throws IOException {
    return mapper.readValue(arr, startPos, len, new TypeReference<Map<String, Object>>() {
    });
  }
}
