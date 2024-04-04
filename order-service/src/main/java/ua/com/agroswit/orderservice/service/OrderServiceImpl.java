package ua.com.agroswit.orderservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ua.com.agroswit.orderservice.dto.OrderDTO;
import ua.com.agroswit.orderservice.dto.OrderHistoryDTO;
import ua.com.agroswit.orderservice.dto.mapper.OrderMapper;
import ua.com.agroswit.orderservice.exception.ResourceNotFoundException;
import ua.com.agroswit.orderservice.repository.OrderHistoryRepository;
import ua.com.agroswit.orderservice.repository.OrderRepository;

import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepo;
    private final OrderHistoryRepository orderHistoryRepo;
    private final OrderMapper mapper;

    @Override
    public List<OrderDTO> getAll() {
        return orderRepo.findAll().stream()
                .map(mapper::toDTO)
                .toList();
    }

    @Override
    public OrderDTO getById(Integer id) {
        return orderRepo.findById(id)
                .map(mapper::toDTO)
                .orElseThrow(() -> new ResourceNotFoundException(String.format(
                        "Order with id %d not found", id))
                );
    }

    @Override
    public List<OrderHistoryDTO> getHistoryById(Integer id) {
        return Collections.emptyList();
    }
}
