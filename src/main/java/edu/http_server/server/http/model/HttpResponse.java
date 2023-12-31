package edu.http_server.server.http.model;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Data
public class HttpResponse {
    //version
    private HttpVersion httpVersion = HttpVersion.HTTP_1_1;
    private HttpStatus httpStatus;

    //headers
    private Map<String, String> headers = new HashMap<>();

    //body
    private String body;

    public String toResponseString() {
        return httpVersion.getValue() + " " + httpStatus.getFullStatus() + "\n" + getHeadersString() + "\n\n" + body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public void setStatusCode(int code) {
        this.httpStatus = HttpStatus.getStatus(code);
    }

    public String getHeadersString(){
        return headers.entrySet().stream()
                .map(entry -> entry.getKey() + ":" + entry.getValue())
                .collect(Collectors.joining("\n"));
    }
}
