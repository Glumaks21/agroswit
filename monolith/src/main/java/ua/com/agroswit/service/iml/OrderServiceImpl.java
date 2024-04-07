package ua.com.agroswit.service.iml;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ua.com.agroswit.dto.OrderDTO;
import ua.com.agroswit.dto.mappers.OrderMapper;
import ua.com.agroswit.repository.OrderRepository;
import ua.com.agroswit.service.OrderService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository repository;
    private final OrderMapper mapper;

    @Override
    public List<OrderDTO> getAll() {
        return repository.findAll().stream()
                .map(mapper::toDTO)
                .toList();
    }
}
