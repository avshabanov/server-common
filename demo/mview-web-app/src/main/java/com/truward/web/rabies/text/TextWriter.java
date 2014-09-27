package com.truward.web.rabies.text;

import javax.annotation.Nonnull;
import java.io.Closeable;
import java.io.IOException;

/**
 * @author Alexander Shabanov
 */
public interface TextWriter extends Closeable {

  @Nonnull
  TextWriter append(@Nonnull CharSequence value) throws IOException;

  @Nonnull
  TextWriter append(char value) throws IOException;

  @Nonnull
  TextWriter append(int value) throws IOException;

  @Nonnull
  TextWriter append(long value) throws IOException;

  // add UTF-8 - encoded values
  @Nonnull
  TextWriter append(byte[] utfBuffer, int offset, int length) throws IOException;

  @Nonnull
  TextWriter append(byte[] utfBuffer) throws IOException;
}
