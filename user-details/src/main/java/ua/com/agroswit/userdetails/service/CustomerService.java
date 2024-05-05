package ua.com.agroswit.userdetails.service;

import org.apache.commons.lang3.tuple.Pair;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ua.com.agroswit.userdetails.dto.response.CustomerDTO;
import ua.com.agroswit.userdetails.dto.response.UserServiceDTO;
import ua.com.agroswit.userdetails.model.Customer;

import java.util.List;

public interface CustomerService {
    Page<CustomerDTO> getAll(Pageable pageable);
    CustomerDTO getById(Integer id);
    UserServiceDTO fetchUser(Customer customer);
    <T extends Customer> List<Pair<T, UserServiceDTO>> fetchUsers(List<T> customers);
}
