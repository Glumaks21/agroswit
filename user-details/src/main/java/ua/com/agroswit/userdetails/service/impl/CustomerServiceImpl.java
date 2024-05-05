package ua.com.agroswit.userdetails.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.com.agroswit.userdetails.client.UserClient;
import ua.com.agroswit.userdetails.dto.mapper.CustomerMapper;
import ua.com.agroswit.userdetails.dto.response.CustomerDTO;
import ua.com.agroswit.userdetails.dto.response.UserServiceDTO;
import ua.com.agroswit.userdetails.exception.ResourceNotFoundException;
import ua.com.agroswit.userdetails.model.Customer;
import ua.com.agroswit.userdetails.repository.CustomerRepository;
import ua.com.agroswit.userdetails.service.CustomerService;

import java.util.List;

import static java.util.stream.Collectors.toSet;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {

    private final UserClient userClient;
    private final CustomerRepository repo;
    private final CustomerMapper mapper;


    @Override
    @Transactional(readOnly = true)
    public Page<CustomerDTO> getAll(Pageable pageable) {
        var customerPage = repo.findAll(pageable);
        var pairs = fetchUsers(customerPage.getContent());
        var dtos = pairs.stream().map(p -> mapper.toDTO(p.getLeft(), p.getRight())).toList();
        return new PageImpl<>(dtos, pageable, customerPage.getTotalElements());
    }

    @Override
    @Transactional(readOnly = true)
    public CustomerDTO getById(Integer id) {
        var customer = repo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Customer with id %d not found".formatted(id)));
        var user = fetchUser(customer);
        return mapper.toDTO(customer, user);
    }

    @Override
    public UserServiceDTO fetchUser(Customer customer) {
        return userClient.getById(customer.getUserId());
    }

    @Override
    public <T extends Customer> List<Pair<T, UserServiceDTO>> fetchUsers(List<T> customers) {
        var userIds = customers.stream().map(Customer::getUserId).collect(toSet());
        var users = userClient.getByIds(userIds);

        return customers.stream()
                .map(c -> {
                    var user = users.stream()
                            .filter(u -> c.getUserId().equals(u.id()))
                            .findFirst()
                            .orElseThrow(() -> new IllegalArgumentException(
                                    "User with id %d not found for customer".formatted(c.getUserId()))
                            );
                    return Pair.of(c, user);
                })
                .toList();
    }
}
