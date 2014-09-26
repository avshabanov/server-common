package com.truward.web.rabies.support;

import com.truward.web.rabies.text.TextWriter;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;

/**
 * @author Alexander Shabanov
 */
public class StreamTextWriter implements TextWriter {
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
  public TextWriter append(int value) throws IOException {
    writer.append(Integer.toString(value));
    return this;
  }

  @Nonnull
  @Override
  public TextWriter append(long value) throws IOException {
    writer.append(Long.toString(value));
    return this;
  }

  @Override
  public void close() throws IOException {
    writer.close();
  }
}
