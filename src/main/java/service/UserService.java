package service;

import dao.UserDAO;
import model.User;

public class UserService {

    private final UserDAO userDAO;

    public UserService(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    public void register(User user) {
        userDAO.save(user);
    }

    public User login(String email) {
        return userDAO.findByEmail(email);
    }
}