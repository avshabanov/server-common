package com.alexshabanov.mwa.web;

import com.alexshabanov.mwa.service.DefaultSampleService;
import com.alexshabanov.mwa.service.SampleService;
import com.alexshabanov.mwa.web.controller.PublicResource;
import com.alexshabanov.mwa.web.controller.RestResource;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.TypeLiteral;
import com.google.inject.servlet.GuiceServletContextListener;
import com.sun.jersey.guice.JerseyServletModule;
import com.sun.jersey.guice.spi.container.servlet.GuiceContainer;
import org.codehaus.jackson.jaxrs.JacksonJsonProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.ext.MessageBodyReader;
import javax.ws.rs.ext.MessageBodyWriter;

/**
 * @author Alexander Shabanov
 */
public final class GuiceServletConfig extends GuiceServletContextListener {
  private final Logger log = LoggerFactory.getLogger(getClass());

  @Override
  protected Injector getInjector() {
    return Guice.createInjector(new JerseyServletModule() {
      @Override
      protected void configureServlets() {
        log.info("MVIEW -- Configuring servlets");

        // REST resources
        bind(RestResource.class);
        bind(PublicResource.class);

        // reader/writer
        bind(new TypeLiteral<MessageBodyReader<Object>>() {}).to(new TypeLiteral<JacksonJsonProvider>() {});
        bind(new TypeLiteral<MessageBodyWriter<Object>>() {}).to(new TypeLiteral<JacksonJsonProvider>() {});

        // services
        bind(SampleService.class).to(DefaultSampleService.class);

        serve("/*").with(GuiceContainer.class);
      }
    });
  }
}
