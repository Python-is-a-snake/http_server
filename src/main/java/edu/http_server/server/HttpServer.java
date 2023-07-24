package edu.http_server.server;

import edu.http_server.server.http.RequestHandler;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.net.ServerSocket;
import java.net.Socket;


@Slf4j
public class HttpServer {
    private final RequestHandler requestHandler;
    private final int port;

    public HttpServer(RequestHandler requestHandler, int port) {
        this.requestHandler = requestHandler;
        this.port = port;
    }

    @SneakyThrows
    public void start() {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            while (true) {
                log.debug("Waiting for connection...");
                Socket socket = serverSocket.accept();
                log.debug("Client connected, port: {}", socket.getPort());

                requestHandler.handleRequest(socket);
            }
        }
    }
}
