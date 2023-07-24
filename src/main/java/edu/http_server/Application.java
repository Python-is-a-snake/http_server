package edu.http_server;

import edu.http_server.server.ApplicationContext;
import edu.http_server.server.HttpServer;
import edu.http_server.server.http.RequestHandler;

public class Application {
    public static void main(String[] args) {
        // init components and dependencies
//        ApplicationContext.init();
        // init mappings
        RequestHandler requestHandler = new RequestHandler();
//        requestHandler.initMappings(ApplicationContext.getInstance());
        // start server
        new HttpServer(requestHandler, 8080).start();
    }
}
