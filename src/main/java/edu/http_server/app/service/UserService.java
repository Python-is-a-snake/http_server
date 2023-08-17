package edu.http_server.app.service;

import edu.http_server.app.dao.UserDao;
import edu.http_server.app.model.entity.User;
import edu.http_server.server.di.Component;

// todo: implement base CRUD operation methods
public class UserService {
    private final UserDao userDao;

    public UserService(UserDao userDao) {
        this.userDao = userDao;
    }

    public User createUser(User user) {
        return userDao.save(user);
    }
}
