package edu.http_server.app.service;

import edu.http_server.app.dao.UserDao;
import edu.http_server.app.model.entity.User;
import edu.http_server.server.di.Service;

import java.util.List;

@Service
public class UserService {
    private final UserDao userDao;

    public UserService(UserDao userDao) {
        this.userDao = userDao;
    }

    public User createUser(User user) {
        return userDao.save(user);
    }
    public User findUserById(Long id) {
        return userDao.findById(id).orElseThrow();
    }
    public boolean deleteById(Long id) {
        return userDao.deleteById(id);
    }

    public List<User> findAll() {
        return userDao.findAll();
    }

    public User updateUser(Long id, User dataToUpdate) {
        User user = userDao.findById(id).orElseThrow();

        if (dataToUpdate.getName() != null) {
            user.setName(dataToUpdate.getName());
        }

        if (dataToUpdate.getEmail() != null) {
            user.setEmail(dataToUpdate.getEmail());
        }

        return userDao.update(user);
    }
}
