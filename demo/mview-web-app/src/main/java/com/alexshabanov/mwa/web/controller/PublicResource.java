package com.alexshabanov.mwa.web.controller;

import com.alexshabanov.mwa.web.view.HelloPageHtmlView;
import com.truward.web.rabies.jersey.TextStreamingOutput;
import com.truward.web.rabies.support.StandardMediaTypes;
import com.truward.web.rabies.support.view.StaticResourceView;
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

  private final TextView helloPage = new HelloPageHtmlView();
  private final TextView logging = StaticResourceView.fromResourcePath("logging.properties");

  @GET
  @Produces(StandardMediaTypes.TEXT_HTML_UTF8)
  @Path("/page.html")
  public StreamingOutput page() {
    return TextStreamingOutput.from(helloPage);
  }

  @GET
  @Produces(StandardMediaTypes.TEXT_PLAIN_UTF8)
  @Path("/logging.txt")
  public StreamingOutput logging() {
    return TextStreamingOutput.from(logging);
  }
}
