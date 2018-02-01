package main;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.ResourceHandler;
import servlets.SearchServlet;

/**
 * Created by SBT-Meshcheryakov-AA on 15.01.2018.
 */
public class Main {
    public static void main(String[] args) throws Exception {
        SearchServlet searchServlet = new SearchServlet();

        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.addServlet(new ServletHolder(searchServlet), "/search");

        ResourceHandler resourceHandler = new ResourceHandler();
        resourceHandler.setResourceBase("public_html");

        HandlerList handlers = new HandlerList();
        handlers.setHandlers(new Handler[]{resourceHandler, context});

        Server server = new Server(8091);
        server.setHandler(handlers);

        server.start();
        server.join();
    }
}
