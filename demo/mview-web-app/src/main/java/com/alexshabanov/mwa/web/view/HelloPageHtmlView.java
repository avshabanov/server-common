package com.alexshabanov.mwa.web.view;

import com.truward.web.rabies.text.TextView;
import com.truward.web.rabies.text.TextWriter;
import com.truward.web.rabies.text.Model;

import javax.annotation.Nonnull;
import java.io.IOException;

/**
 * @author Alexander Shabanov
 */
public final class HelloPageHtmlView implements TextView {

  @Override
  public void render(@Nonnull TextWriter writer, @Nonnull Model model) throws IOException {
    writer
        .append("<!doctype html>\n<html>\n");

    writer
        .append("<head>\n")
        .append("<title>Hello!</title>\n")
        .append("</head>\n");

    writer.append("<body>\n");
    writer.append("<p>Hello there!</p>\n");
    writer.append("</body>\n");

    writer.append("</html>\n");
  }
}
