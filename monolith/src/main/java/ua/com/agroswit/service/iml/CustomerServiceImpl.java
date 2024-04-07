package ua.com.agroswit.service.iml;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ua.com.agroswit.dto.CustomerDTO;
import ua.com.agroswit.dto.mappers.CustomerMapper;
import ua.com.agroswit.exception.ResourceNotFoundException;
import ua.com.agroswit.repository.CustomerRepository;
import ua.com.agroswit.service.CustomerService;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepo;
    private final CustomerMapper mapper;

    @Override
    public List<CustomerDTO> getAll(Pageable pageable) {
        return customerRepo.findAll(pageable).stream()
                .map(mapper::mapToDTO)
                .toList();
    }

    @Override
    public CustomerDTO getById(Integer id) {
        return customerRepo.findById(id)
                .map(mapper::mapToDTO)
                .orElseThrow(() -> new ResourceNotFoundException(String.format(
                        "Customer with id %d not found", id))
                );
    }
}
