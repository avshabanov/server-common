package com.alexshabanov.mwa;

import com.alexshabanov.mwa.web.GuiceServletConfig;
import com.google.inject.servlet.GuiceFilter;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.nio.SelectChannelConnector;
import org.eclipse.jetty.servlet.DefaultServlet;
import org.eclipse.jetty.servlet.FilterHolder;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.webapp.WebAppContext;

import javax.servlet.DispatcherType;
import java.util.EnumSet;

/**
 * Entry point.
 */
public final class Launcher {

  public static void main(String[] args) throws Exception {
    final Server server = new Server(8080);

    // servlet context holder - no sessions, no security support
    final ServletContextHandler context = new ServletContextHandler(ServletContextHandler.NO_SESSIONS);
    context.setContextPath("/");

    // guice
    context.addEventListener(new GuiceServletConfig());
    context.addFilter(GuiceFilter.class, "/*", EnumSet.allOf(DispatcherType.class));

    final ServletHolder defaultServletHolder = context.addServlet(DefaultServlet.class, "/");
    defaultServletHolder.setInitParameter("resourceBase", "src/main/webapp"); // <-- serve static resources fromResourcePath this

    // set server handler
    server.setHandler(context);

    // start server
    server.start();
    server.join();
  }
}
