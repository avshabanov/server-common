package com.truward.web.rabies.text;

import javax.annotation.Nonnull;
import java.io.IOException;

/**
 * @author Alexander Shabanov
 */
public interface TextView {

  void render(@Nonnull TextWriter writer, @Nonnull Model model) throws IOException;
}
