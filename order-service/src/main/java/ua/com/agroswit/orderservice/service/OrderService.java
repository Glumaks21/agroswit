package ua.com.agroswit.orderservice.service;

import ua.com.agroswit.orderservice.dto.OrderDTO;
import ua.com.agroswit.orderservice.dto.OrderHistoryDTO;

import java.util.List;

public interface OrderService {
    List<OrderDTO> getAll();
    OrderDTO getById(Integer id);
    List<OrderHistoryDTO> getHistoryById(Integer id);
}
