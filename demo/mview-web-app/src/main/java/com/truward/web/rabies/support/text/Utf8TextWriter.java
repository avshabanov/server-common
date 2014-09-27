package com.truward.web.rabies.support.text;

import com.truward.web.rabies.text.TextWriter;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.io.OutputStream;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * @author Alexander Shabanov
 */
public class Utf8TextWriter extends AbstractTextWriter {
  private final OutputStream outputStream;

  public Utf8TextWriter(@Nonnull OutputStream stream) {
    this.outputStream = checkNotNull(stream, "stream");
  }

  @Nonnull
  @Override
  public TextWriter append(@Nonnull CharSequence value) throws IOException {
    final int size = value.length();
    for (int i = 0; i < size; ++i) {
      append(value.charAt(i));
    }
    return this;
  }

  @Nonnull
  @Override
  public TextWriter append(char ch) throws IOException {
    if (0 <= ch && ch <= 0x7F) {
      outputStream.write((byte) ch);
    } else if (ch <= 0x7FF) {
      outputStream.write((byte)(0xC0 | (ch >> 6)));
      outputStream.write((byte)(0x80 | (ch & 0x3F)));
    } else {
      // TODO: higher order bytes?
      outputStream.write((byte)(0xE0 | (ch >> 12)));
      outputStream.write((byte)(0x80 | ((ch >> 6) & 0x3F)));
      outputStream.write((byte)(0x80 | (ch & 0x3F)));
    }
    return this;
  }

  @Nonnull
  @Override
  public TextWriter append(byte[] utfBuffer, int offset, int length) throws IOException {
    outputStream.write(utfBuffer, offset, length);
    return this;
  }

  @Override
  public void close() throws IOException {
    outputStream.close();
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
