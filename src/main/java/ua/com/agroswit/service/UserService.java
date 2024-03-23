package ua.com.agroswit.service;


import ua.com.agroswit.model.User;

import java.util.List;
import java.util.Optional;

public interface UserService {
    Optional<User> getById(Integer id);
    Optional<User> getByLogin(String name);
    List<User> getAll();
}
