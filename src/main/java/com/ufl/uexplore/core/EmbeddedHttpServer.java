package com.ufl.uexplore.core;

import java.util.concurrent.Callable;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.webapp.WebAppContext;

public class EmbeddedHttpServer implements Callable<Integer>{
	
	private String serverPort = "5000";
	private String contextPath = "/";
	private String webAppLocation = "src/main/webapp/";
	private String descriptorLocation = webAppLocation + "/WEB-INF/web.xml";


	@Override
	public Integer call() throws Exception {
		final Server server = new Server(Integer.valueOf(serverPort));
        final WebAppContext root = new WebAppContext(webAppLocation, contextPath);
        root.setParentLoaderPriority(true);
        root.setDescriptor(descriptorLocation );
        server.setHandler(root);
        server.start();
        server.setStopAtShutdown(true);
		return 0;
	}

	
	
}
