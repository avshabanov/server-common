package com.alexshabanov.mwa.web.controller;

import com.alexshabanov.mwa.service.SampleService;
import com.google.inject.Inject;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Test: http://127.0.0.1:8080/rest/hello
 *
 * @author Alexander Shabanov
 */
@Path("/rest")
public final class RestResource {
  @Inject
  private SampleService sampleService;

  @GET
  @Produces(MediaType.TEXT_PLAIN)
  @Path("/hello")
  public String getHello() {
    return "Hello from Jersey Resource!";
  }



  @GET
  @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
  @Path("/message/{param}")
  public SampleMessage getMessage(@PathParam("param") String param) {
    return SampleMessage.from(param);
  }

  @GET
  @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
  @Path("/letter/{param}")
  public SampleMessage getLetterText(@PathParam("param") int param) {
    return SampleMessage.from(sampleService.getGreeting(param));
  }

  @XmlRootElement
  public static final class SampleMessage {
    public String text;

    public static SampleMessage from(String text) {
      final SampleMessage message = new SampleMessage();
      message.text = text;
      return message;
    }
  }
}
