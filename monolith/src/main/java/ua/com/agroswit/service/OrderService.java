package ua.com.agroswit.service;

import ua.com.agroswit.dto.OrderDTO;

import java.util.List;

public interface OrderService {
    List<OrderDTO> getAll();
}
