package edu.http_server.server.http.model;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public class HttpRequest {
    // head
    private HttpMethod method;
    private String url;
    private HttpVersion version;
    // headers
    private Map<String, String> headers = new HashMap<>();
    // body
    private String body;

    public void addHeader(String name, String value) {
        headers.put(name, value);
    }
}
