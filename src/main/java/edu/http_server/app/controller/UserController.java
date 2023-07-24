package edu.http_server.app.controller;

import edu.http_server.app.model.entity.User;
import edu.http_server.server.di.Component;
import edu.http_server.server.http.mapping.RequestMapping;

import java.util.List;

import static edu.http_server.server.http.model.HttpMethod.GET;

@Component
public class UserController {

    @RequestMapping(method = GET, url = "/api/users")
    public List<User> getAll() {
        // TODO
        return null;
    }

}
