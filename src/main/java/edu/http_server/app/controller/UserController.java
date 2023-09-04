package edu.http_server.app.controller;

import edu.http_server.app.service.UserService;
import edu.http_server.app.model.entity.User;
import edu.http_server.server.di.Controller;
import edu.http_server.server.http.mapping.PathVariable;
import edu.http_server.server.http.mapping.RequestBody;
import edu.http_server.server.http.mapping.RequestMapping;
import edu.http_server.server.http.mapping.RequestParam;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

import static edu.http_server.server.http.model.HttpMethod.*;

@Controller
@Slf4j
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @RequestMapping(method = POST, url = "/api/users")
    public User createUser(@RequestBody User user){
        log.debug("Request caught, user value = {}", user);
        return userService.createUser(user);
    }

    @RequestMapping(method = GET, url = "/api/users/{id}")
    // should map to "/api/users/1", etc.)
    public User findById(@PathVariable(name = "id") Long id, @RequestParam(name = "banned", required = false) Boolean isBanned){
        log.debug("Request caught, id value = {}, is banned = {}", id, isBanned);
        return userService.findUserById(id);
    }

    @RequestMapping(method = GET, url = "/api/users")
    public List<User> findAll() {
        log.debug("getAll() proceeded");
        return userService.findAll();
    }

    @RequestMapping(method = PUT, url = "/api/users/{id}")
    public User updateById(@PathVariable(name = "id") Long id, @RequestBody User user){
        log.debug("Request caught, user value = {}", user);
        return userService.updateUser(id, user);
    }

    @RequestMapping(method = DELETE, url = "/api/users/{id}")
    public boolean deleteById(@PathVariable(name = "id") Long id){
        log.debug("Request caught, id value = {}", id);
        return userService.deleteById(id);
    }
}
