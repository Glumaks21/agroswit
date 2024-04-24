package ua.com.agroswit.authservice.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ua.com.agroswit.authservice.dto.response.UserDTO;


public interface UserService {
    Page<UserDTO> getAll(Pageable pageable);
    UserDTO getById(Integer id);
}
