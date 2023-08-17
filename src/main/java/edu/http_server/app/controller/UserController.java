package edu.http_server.app.controller;

import edu.http_server.app.service.UserService;
import edu.http_server.app.model.entity.User;
import edu.http_server.server.di.Controller;
import edu.http_server.server.http.mapping.PathVariable;
import edu.http_server.server.http.mapping.RequestBody;
import edu.http_server.server.http.mapping.RequestMapping;
import edu.http_server.server.http.mapping.RequestParam;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

import static edu.http_server.server.http.model.HttpMethod.*;

@Controller
@Slf4j
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @RequestMapping(method = GET, url = "/api/users")
    public List<User> getAll() {
        List<User> users = new ArrayList<>();
        log.debug("getAll() proceeded");
        return null;
    }

    @RequestMapping(method = GET, url = "/api/users/{id}")
    // should map to "/api/users/1", etc.)
    public User getById(@PathVariable(name = "id") Long id, @RequestParam(name = "banned", required = true) Boolean isBanned){
        log.debug("Request caught, id value = {}, is banned = {}", id, isBanned);
        return null;
    }

    @RequestMapping(method = POST, url = "/api/users")
    public User createUser(@RequestBody User user){
        log.debug("Request caught, user value = {}", user);
        return userService.createUser(user);
    }

    @RequestMapping(method = GET, url = "/api/users?banned=true")
    public User findUserBanned(@RequestBody User user){
        log.debug("Request caught, user value = {}", user);
        return user;
    }
}
