package edu.http_server.server.http;

import edu.http_server.server.http.model.HttpMethod;
import edu.http_server.server.http.model.HttpRequest;
import lombok.SneakyThrows;

import java.io.BufferedReader;

public class HttpRequestParser {

    @SneakyThrows
    public HttpRequest parseRequest(BufferedReader reader) {
        // TODO implement
        HttpRequest httpRequest = new HttpRequest();

        // 1 - parse first line
        String[] firstLine = reader.readLine().split(" ");
        HttpMethod method = HttpMethod.valueOf(firstLine[0]);

        // 2 - parse headers
        String header = reader.readLine();
        String[] split = header.split(":");


        // 3 - parse body if present

        return httpRequest;
    }
}
