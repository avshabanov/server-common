package com.truward.web.rabies.jersey;

import com.truward.web.rabies.support.text.Utf8TextWriter;
import com.truward.web.rabies.text.Model;
import com.truward.web.rabies.text.TextView;
import com.truward.web.rabies.text.TextWriter;

import javax.annotation.Nonnull;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.StreamingOutput;
import java.io.IOException;
import java.io.OutputStream;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * @author Alexander Shabanov
 */
public abstract class TextStreamingOutput implements StreamingOutput {

  //
  // Static convenience methods
  //

  @Nonnull
  public static TextStreamingOutput from(@Nonnull final TextView textView, @Nonnull final Model model) {
    return new TextStreamingOutput() {
      @Override
      public void write(@Nonnull TextWriter textWriter) throws IOException {
        textView.render(textWriter, model);
      }
    };
  }

  @Nonnull
  public static TextStreamingOutput from(@Nonnull TextView textView) {
    return from(textView, Model.of());
  }

  //
  // Public overridden functionality
  //

  @Override
  public final void write(OutputStream outputStream) throws IOException, WebApplicationException {
    try (final TextWriter textWriter = createTextWriter(outputStream)) {
      write(textWriter);
    }
  }

  //
  // Protected overridable methods
  //

  protected abstract void write(@Nonnull TextWriter textWriter) throws IOException, WebApplicationException;

  @Nonnull
  protected TextWriter createTextWriter(OutputStream outputStream) {
    return new Utf8TextWriter(checkNotNull(outputStream, "outputStream"));
  }
}
