package ua.com.agroswit.orderservice.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ua.com.agroswit.orderservice.dto.OrderDTO;
import ua.com.agroswit.orderservice.dto.OrderHistoryDTO;

import java.util.List;
import java.util.Set;

public interface OrderService {
    Page<OrderDTO> getAll(Pageable pageable);
    OrderDTO getById(Integer id);
    List<OrderDTO> getByIds(Set<Integer> ids);
    List<OrderHistoryDTO> getHistoryById(Integer id);
    OrderDTO create(OrderDTO dto);
}
