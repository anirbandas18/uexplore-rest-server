package com.ufl.uexplore;

import java.util.concurrent.ExecutionException;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.glassfish.jersey.servlet.ServletContainer;

public class UExplore {

	public static void main(String[] args) throws InterruptedException, ExecutionException {
		Server jettyServer = new Server(5001);
		ServletContextHandler context = new ServletContextHandler(ServletContextHandler.NO_SESSIONS | ServletContextHandler.SECURITY);
		context.setContextPath("/");
		jettyServer.setHandler(context);
		ServletHolder serHol = context.addServlet(ServletContainer.class, "/");
        serHol.setInitOrder(1);
        serHol.setInitParameter("jersey.config.server.provider.packages", 
                "com.ufl.uexplore.controller");
		try {
			jettyServer.start();
			jettyServer.join();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}