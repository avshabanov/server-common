package com.truward.web.rabies.support;

import javax.annotation.Nonnull;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

/**
 * @author Alexander Shabanov
 */
public class Utf8TextWriter extends StreamTextWriter { // TODO: do not extend StreamTextWriter, write UTF-8 directly

  public Utf8TextWriter(@Nonnull OutputStream stream) {
    // TODO: avoid using writer - write to stream directly
    super(stream, StandardCharsets.UTF_8);
  }

//  @Nonnull
//  @Override
//  public TextWriter append(@Nonnull CharSequence value) throws IOException {
//    writer.append(value);
//    return this;
//  }
//
//  @Nonnull
//  @Override
//  public TextWriter append(char value) throws IOException {
//    writer.append(value);
//    return this;
//  }
//
//  @Nonnull
//  @Override
//  public TextWriter append(int value) throws IOException {
//    writer.append(Integer.toString(value)); // TODO: inline transformation
//    return this;
//  }
//
//  @Nonnull
//  @Override
//  public TextWriter append(long value) throws IOException {
//    writer.append(Long.toString(value)); // TODO: inline transformation
//    return this;
//  }
}
