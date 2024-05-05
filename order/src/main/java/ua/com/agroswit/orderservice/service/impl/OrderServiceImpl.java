package ua.com.agroswit.orderservice.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.com.agroswit.orderservice.dto.OrderDTO;
import ua.com.agroswit.orderservice.dto.OrderHistoryDTO;
import ua.com.agroswit.orderservice.dto.mapper.OrderHistoryMapper;
import ua.com.agroswit.orderservice.dto.mapper.OrderMapper;
import ua.com.agroswit.orderservice.exception.ResourceNotFoundException;
import ua.com.agroswit.orderservice.model.OrderHistory;
import ua.com.agroswit.orderservice.repository.OrderHistoryRepository;
import ua.com.agroswit.orderservice.repository.OrderRepository;
import ua.com.agroswit.orderservice.service.OrderService;

import java.util.List;
import java.util.Set;

import static ua.com.agroswit.orderservice.model.enums.OrderState.ABORTED;
import static ua.com.agroswit.orderservice.model.enums.OrderState.PROCEED;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepo;
    private final OrderHistoryRepository orderHistoryRepo;
    private final OrderMapper orderMapper;
    private final OrderHistoryMapper historyMapper;


    @Override
    @Transactional(readOnly = true)
    public Page<OrderDTO> getAll(Pageable pageable) {
        return orderRepo.findAll(pageable).map(orderMapper::toDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public OrderDTO getById(Integer id) {
        return orderRepo.findById(id)
                .map(orderMapper::toDTO)
                .orElseThrow(() -> new ResourceNotFoundException("Order with id %d not found".formatted(id)));
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrderDTO> getByIds(Set<Integer> ids) {
        return orderMapper.toDTOs(orderRepo.findAllById(ids));
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrderHistoryDTO> getHistoryById(Integer id) {
        if (!orderRepo.existsById(id)) {
            throw new ResourceNotFoundException("Order with id %d not exists".formatted(id));
        }
        return historyMapper.toDTOs(orderHistoryRepo.findAllByOrderId(id));
    }

    @Override
    @Transactional
    public OrderDTO create(OrderDTO dto) {
        var order = orderMapper.toEntity(dto);
        return orderMapper.toDTO(orderRepo.save(order));
    }

    @Transactional
    public void abort(Integer id, String description) {
        var order = orderRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order with id %d not found".formatted(id)));

        var history = new OrderHistory();
        history.setOrder(order);
        history.setState(ABORTED);
        history.setDescription(description);
        history.setUserId(1);

        log.info("Saving new order history: {}", history);
        orderHistoryRepo.save(history);

        log.info("Updating order with id {} to state {}", id, ABORTED);
        orderRepo.updateOrderStateBy(id, ABORTED);
    }

    @Transactional
    public void proceed(Integer id, Integer managerId) {
        var order = orderRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order with id %d not found".formatted(id)));

        var history = new OrderHistory();
        history.setOrder(order);
        history.setState(PROCEED);
        history.setUserId(1);

        log.info("Saving new order history: {}", history);
        orderHistoryRepo.save(history);

        log.info("Updating order with id {} to state {}", id, PROCEED);
        orderRepo.updateOrderStateBy(id, ABORTED);
    }
}
