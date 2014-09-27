package com.truward.web.rabies.support.view;

import com.truward.web.rabies.text.Model;
import com.truward.web.rabies.text.TextView;
import com.truward.web.rabies.text.TextWriter;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

/**
 * Reads resource and wraps it as a text view.
 *
 * @author Alexander Shabanov
 */
public final class StaticResourceView implements TextView {
  private static final int INITIAL_BUF_LENGTH = 8192;

  private final byte[] content;

  private StaticResourceView(@Nonnull InputStream inputStream) throws IOException {
    byte[] buffer = new byte[INITIAL_BUF_LENGTH];
    int pos = 0;

    for (;;) {
      final int read = inputStream.read(buffer, pos, buffer.length - pos);
      if (read < 0) {
        break;
      }

      pos += read;
      if (buffer.length == pos) {
        // realloc the buffer
        buffer = Arrays.copyOf(buffer, buffer.length * 2);
      }
    }

    this.content = Arrays.copyOf(buffer, pos);
  }

  @Nonnull
  public static TextView fromResourcePath(@Nonnull String resourcePath) {
    try {
      final InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(resourcePath);
      if (inputStream == null) {
        throw new IllegalStateException("There is no resource=" + resourcePath);
      }

      return new StaticResourceView(inputStream);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public void render(@Nonnull TextWriter writer, @Nonnull Model model) throws IOException {
    writer.append(content);
  }
}
