package edu.http_server.server.http;

import edu.http_server.server.ApplicationContext;
import edu.http_server.server.di.Component;
import edu.http_server.server.http.exception.BadRequestException;
import edu.http_server.server.http.exception.NotFoundException;
import edu.http_server.server.http.mapping.RequestMapping;
import edu.http_server.server.http.model.HttpRequest;
import edu.http_server.server.http.model.HttpResponse;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.net.Socket;
import java.util.List;

@Slf4j
@Component
public class RequestHandler {

    @SneakyThrows
    public void handleRequest(Socket socket) {
        try (socket;
             BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter writer = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()))
        ) {
            // 1 - read and validate input request
            HttpRequest httpRequest = new HttpRequestParser().parseRequest(reader);
            HttpResponse httpResponse = new HttpResponse();

            // 2 - handle request
//            service(httpRequest, httpResponse);

            // 3 - write response
            writer.println(httpResponse.toResponseString());
        }
    }

    @SneakyThrows
    private void service(HttpRequest httpRequest, HttpResponse httpResponse) {
        try {
            // 1 - find appropriate mapping that match http method and url
            RequestMapping mapping = findMapping(httpRequest);
            Method method = null;
            Object controller = null;

            // 2 - get method arguments
            Object[] args = getMethodArgs(method.getParameters(), mapping, httpRequest);

            Object returnObject = method.invoke(controller, args);
            httpResponse.setBody(returnObject.toString());
            httpResponse.setStatusCode(200);
        } catch (NotFoundException e) {
            httpResponse.setStatusCode(404);
        } catch (BadRequestException e) {
            httpResponse.setStatusCode(400);
        }
    }

    private Object[] getMethodArgs(Parameter[] parameters, RequestMapping mapping, HttpRequest httpRequest) throws BadRequestException {
        return new Object[0];
    }

    private RequestMapping findMapping(HttpRequest httpRequest) throws NotFoundException {
        return null;
    }

    public void initMappings(ApplicationContext applicationContext) {
        List<Object> controllers = applicationContext.getBeansAnnotatedWith(Component.class);
        // TODO mappings - implement initialization logic
    }
}
