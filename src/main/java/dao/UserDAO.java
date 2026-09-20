package dao;

import model.User;
import java.util.List;

public interface UserDAO {

    void save(User user);

    User findByEmail(String email);

    User findById(int id);

    List<User> findAll();
}