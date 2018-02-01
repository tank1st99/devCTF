package main;

import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import servlets.HelloServlet;
import servlets.OisServlet;

/**
 * Created by SBT-Meshcheryakov-AA on 15.01.2018.
 */
public class Main {
    public static void main(String[] args) throws Exception {
        HelloServlet helloServlet = new HelloServlet();
        OisServlet oisServlet = new OisServlet();

        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.addServlet(new ServletHolder(helloServlet), "/");
        context.addServlet(new ServletHolder(oisServlet), "/ois");

        HandlerList handlers = new HandlerList();
        handlers.setHandlers(new Handler[]{context});

        Server server = new Server(8091);
        server.setHandler(handlers);

        server.start();
        server.join();
    }
}
