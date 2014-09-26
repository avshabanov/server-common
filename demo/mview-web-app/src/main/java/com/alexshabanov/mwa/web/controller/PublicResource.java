package com.alexshabanov.mwa.web.controller;

import com.alexshabanov.mwa.web.util.TextStreamingOutput;
import com.alexshabanov.mwa.web.view.HelloPageHtmlView;
import com.truward.web.rabies.support.StandardMediaTypes;
import com.truward.web.rabies.text.TextView;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.StreamingOutput;

/**
 * Test: http://127.0.0.1:8080/rest/hello
 *
 * @author Alexander Shabanov
 */
@Path("/")
public final class PublicResource {

  private final TextView helloPageHtmlView = new HelloPageHtmlView();

  @GET
  @Produces(StandardMediaTypes.TEXT_HTML_UTF8)
  @Path("/page.html")
  public StreamingOutput page() {
    return TextStreamingOutput.from(helloPageHtmlView);
  }
}
