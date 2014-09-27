package com.truward.web.rabies.support.text;

import com.truward.web.rabies.text.TextWriter;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * @author Alexander Shabanov
 */
public class StreamTextWriter extends AbstractTextWriter {
  private OutputStreamWriter writer;

  public StreamTextWriter(@Nonnull OutputStream stream, @Nonnull Charset charset) {
    this.writer = new OutputStreamWriter(stream, charset);
  }

  @Nonnull
  @Override
  public TextWriter append(@Nonnull CharSequence value) throws IOException {
    writer.append(value);
    return this;
  }

  @Nonnull
  @Override
  public TextWriter append(char value) throws IOException {
    writer.append(value);
    return this;
  }

  @Nonnull
  @Override
  public TextWriter append(byte[] utfBuffer, int offset, int length) throws IOException {
    writer.append(new String(utfBuffer, offset, length, StandardCharsets.UTF_8));
    return this;
  }

  @Override
  public void close() throws IOException {
    writer.close();
  }
}
