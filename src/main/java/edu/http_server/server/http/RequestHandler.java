package edu.http_server.server.http;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.http_server.server.ApplicationContext;
import edu.http_server.server.di.Component;
import edu.http_server.server.di.Controller;
import edu.http_server.server.http.exception.BadRequestException;
import edu.http_server.server.http.exception.NotFoundException;
import edu.http_server.server.http.mapping.PathVariable;
import edu.http_server.server.http.mapping.RequestBody;
import edu.http_server.server.http.mapping.RequestMapping;
import edu.http_server.server.http.mapping.RequestParam;
import edu.http_server.server.http.model.HttpMethod;
import edu.http_server.server.http.model.HttpRequest;
import edu.http_server.server.http.model.HttpResponse;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.net.Socket;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class RequestHandler {

    private final ObjectMapper jsonMapper = new ObjectMapper();

    record MethodControllerHolder(Method method, Object controller) {
    }

    private final Map<RequestMapping, MethodControllerHolder> mappingHolder = new HashMap<>();

    @SneakyThrows
    public void handleRequest(Socket socket) {
        try (socket;
             BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter writer = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()))
        ) {
            // 1 - read and validate input request
            if(socket.getInputStream().available() == 0){
                return;
            }

            HttpRequest httpRequest = new HttpRequestParser().parseRequest(socket.getInputStream());
            HttpResponse httpResponse = new HttpResponse();

            // 2 - handle request
            service(httpRequest, httpResponse);

            // 3 - write response
            writer.println(httpResponse.toResponseString());
            writer.flush();

        }
    }

    @SneakyThrows
    private void service(HttpRequest httpRequest, HttpResponse httpResponse) {
        try {
            // 1 - find appropriate mapping that match http method and url
            RequestMapping mapping = findMapping(httpRequest);
            Method method = mappingHolder.get(mapping).method();
            Object controller = mappingHolder.get(mapping).controller();

            // 2 - get method arguments
            Object[] args = getMethodArgs(method.getParameters(), mapping, httpRequest);

            Object returnObject = method.invoke(controller, args);

            if (returnObject != null) {
                String jsonObject = jsonMapper.writeValueAsString(returnObject);
                log.debug("Response body : {}", jsonObject);
                httpResponse.setBody(jsonObject);
            }

            httpResponse.setStatusCode(200);
        } catch (NotFoundException e) {
            httpResponse.setStatusCode(404);
        } catch (BadRequestException e) {
            httpResponse.setStatusCode(400);
        }
    }

    //May be replaced with String
    private Object[] getMethodArgs(Parameter[] parameters, RequestMapping mapping, HttpRequest httpRequest) throws BadRequestException {

        //iterate threw params and check annotation
        Object args[] = new Object[parameters.length];

        for (int i = 0; i < parameters.length; i++) {
            Parameter parameter = parameters[i];

            if (parameter.isAnnotationPresent(PathVariable.class)) {
                args[i] = evaluatePathVariable(mapping, httpRequest, parameter);
            }

            if (parameter.isAnnotationPresent(RequestBody.class)) {
                args[i] = evaluateRequstBody(httpRequest, parameter);
            }

            if (parameter.isAnnotationPresent(RequestParam.class)) {
                args[i] = evaluateRequestParam(httpRequest, parameter);
            }
        }

        return args;
    }

    private Object evaluatePathVariable(RequestMapping mapping, HttpRequest httpRequest, Parameter parameter) {
        String paramValue = "";

        String[] mappingParams = mapping.url().split("/");

        String[] urlParams = httpRequest.getPlainUrl().split("/");

        String pathVarName = parameter.getAnnotation(PathVariable.class).name();

        for (int j = 0; j < mappingParams.length; j++) {
            if (mappingParams[j].startsWith("{") && mappingParams[j].endsWith("}")) {
                if (pathVarName.toLowerCase().equals(mappingParams[j].toLowerCase().substring(1, mappingParams[j].length() - 1)))
                    paramValue = urlParams[j];
            }
        }

        Class<?> parameterType = parameter.getType();
        if (Number.class.isAssignableFrom(parameterType)) {
            return Long.valueOf(paramValue);
        } else return paramValue;
    }

    private Object evaluateRequstBody(HttpRequest httpRequest, Parameter parameter) {
        String body = httpRequest.getBody();

        try {
            if (body.isEmpty()) throw new BadRequestException();
            return jsonMapper.readValue(body, parameter.getType());

        } catch (JsonProcessingException e) {
            throw new BadRequestException();
        }
    }

    private Object evaluateRequestParam(HttpRequest httpRequest, Parameter parameter) {
        RequestParam requestParamAnnotation = parameter.getAnnotation(RequestParam.class);
        String requestParamName = requestParamAnnotation.name();
        String paramValue = "";
        if ((paramValue = httpRequest.getParamValue(requestParamName)) != null) {
            Class<?> parameterType = parameter.getType();

            if (parameterType.equals(String.class)) {
                return paramValue;
            } else if (parameterType.equals(Boolean.class)) {
                return Boolean.valueOf(paramValue);
            } else if (parameterType.equals(Double.class)) {
                return Double.valueOf(paramValue);
            } else if (parameterType.equals(Integer.class)) {
                return Integer.valueOf(paramValue);
            } else if (parameterType.equals(Long.class)) {
                return Long.valueOf(paramValue);
            }
        } else if (!requestParamAnnotation.required()) {
            return null;
        }
        throw new BadRequestException();
    }

    private RequestMapping findMapping(HttpRequest httpRequest) throws NotFoundException {
        String httpUrl = httpRequest.getUrl().contains("?") ? httpRequest.getUrl().substring(0, httpRequest.getUrl().indexOf("?")) : httpRequest.getUrl();
        String[] httpUrlPath = httpUrl.split("/");

        outer:
        for (RequestMapping key : mappingHolder.keySet()) {

            String[] methodUrlPath = key.url().split("/");

            if (httpUrlPath.length != methodUrlPath.length) continue;

            for (int i = 0; i < httpUrlPath.length; i++) {
                if (methodUrlPath[i].startsWith("{") && methodUrlPath[i].endsWith("}")) {
                    continue;
                }
                if (!(httpUrlPath[i].equals(methodUrlPath[i]))) {
                    continue outer;
                }
            }

            if (key.method().equals(httpRequest.getMethod())) {
                return key;
            }
        }
        throw new NotFoundException();
    }

    public void initMappings(ApplicationContext applicationContext) {
        List<Object> controllers = applicationContext.getBeansAnnotatedWith(Controller.class);
        for (Object controller : controllers) {
            Class<?> currentClass = controller.getClass();
            Method[] methods = currentClass.getDeclaredMethods();
            for (Method method : methods) {
                if (method.isAnnotationPresent(RequestMapping.class)) {
                    mappingHolder.put(method.getAnnotation(RequestMapping.class), new MethodControllerHolder(method, controller));
                }
            }
        }
    }
}
