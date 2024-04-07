package ua.com.agroswit.service;


import ua.com.agroswit.dto.UserDTO;
import ua.com.agroswit.model.User;

import java.util.List;
import java.util.Optional;

public interface UserService {
    List<UserDTO> getAll();
    UserDTO getById(Integer id);
}
