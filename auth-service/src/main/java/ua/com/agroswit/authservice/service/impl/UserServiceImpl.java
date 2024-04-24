package ua.com.agroswit.authservice.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.com.agroswit.authservice.dto.mapper.UserMapper;
import ua.com.agroswit.authservice.dto.response.UserDTO;
import ua.com.agroswit.authservice.exception.ResourceNotFoundException;
import ua.com.agroswit.authservice.repository.UserRepository;
import ua.com.agroswit.authservice.service.UserService;


@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepo;
    private final UserMapper mapper;


    @Override
    @Transactional(readOnly = true)
    public Page<UserDTO> getAll(Pageable pageable) {
        return userRepo.findAll(pageable)
                .map(mapper::toDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public UserDTO getById(Integer id) {
        return userRepo.findById(id)
                .map(mapper::toDTO)
                .orElseThrow(() -> new ResourceNotFoundException(String.format(
                        "User with id %d not found", id))
                );

    }
}
