package edu.http_server.server.http.model;

import java.util.Arrays;

public enum HttpVersion {

    HTTP_1_1("HTTP/1.1");
    private final String value;

    HttpVersion(String v) {
        this.value = v;
    }

    public static HttpVersion getVersion(String version){
        return Arrays.stream(HttpVersion.values()).filter(httpVersion -> httpVersion.value.equals(version)).findFirst().get();
    }

    public String getValue(){
        return value;
    }
}
