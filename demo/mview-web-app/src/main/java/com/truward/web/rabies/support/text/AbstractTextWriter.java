package com.truward.web.rabies.support.text;

import com.truward.web.rabies.text.TextWriter;

import javax.annotation.Nonnull;
import java.io.IOException;

/**
 * @author Alexander Shabanov
 */
public abstract class AbstractTextWriter implements TextWriter {

  @Nonnull
  @Override
  public TextWriter append(int value) throws IOException {
    return append(Integer.toString(value)); // TODO: optimized version
  }

  @Nonnull
  @Override
  public TextWriter append(long value) throws IOException {
    return append(Long.toString(value)); // TODO: optimized version
  }

  @Nonnull
  @Override
  public TextWriter append(byte[] utfBuffer) throws IOException {
    return append(utfBuffer, 0, utfBuffer.length);
  }
}
