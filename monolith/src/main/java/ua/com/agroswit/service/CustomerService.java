package ua.com.agroswit.service;

import org.springframework.data.domain.Pageable;
import ua.com.agroswit.dto.CustomerDTO;

import java.util.List;

public interface CustomerService {
    List<CustomerDTO> getAll(Pageable pageable);
    CustomerDTO getById(Integer id);
}
