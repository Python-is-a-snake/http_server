package edu.http_server.server.http.mapping;

import edu.http_server.server.http.model.HttpMethod;

public @interface RequestMapping {
    HttpMethod method();

    String url();
}
