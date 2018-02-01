package main;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.ResourceHandler;
import servlets.BillingServlet;

/**
 * Created by SBT-Meshcheryakov-AA on 22.09.2017.
 */
public class Main {
    public static void main(String[] args) throws Exception {
        BillingServlet billingServlet = new BillingServlet();

        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.addServlet(new ServletHolder(billingServlet), "/bill");

        ResourceHandler resource_handler = new ResourceHandler();
        resource_handler.setResourceBase("public_html");

        HandlerList handlers = new HandlerList();
        handlers.setHandlers(new Handler[]{resource_handler, context});

        Server server = new Server(8091);
        server.setHandler(handlers);

        server.start();
        server.join();
    }
}
