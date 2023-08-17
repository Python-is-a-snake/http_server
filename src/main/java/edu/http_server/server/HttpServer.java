package edu.http_server.server;

import edu.http_server.server.http.RequestHandler;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;


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
        ExecutorService executorService = Executors.newFixedThreadPool(10);

        try (ServerSocket serverSocket = new ServerSocket(port)) {
            while (true) {
                log.debug("Waiting for connection...");
                Socket socket = serverSocket.accept();
                log.debug("Client connected, port: {}", socket.getPort());

                Runnable runnable = () -> requestHandler.handleRequest(socket);
                executorService.submit(runnable);

//                Thread thread = new Thread(runnable, "Client " + socket.getPort());
//                thread.start();
            }
        }
    }
}
