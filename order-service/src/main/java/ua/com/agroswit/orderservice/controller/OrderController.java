package ua.com.agroswit.orderservice.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ua.com.agroswit.orderservice.dto.OrderDTO;
import ua.com.agroswit.orderservice.dto.OrderHistoryDTO;
import ua.com.agroswit.orderservice.service.OrderService;

import java.util.Collection;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Tag(name = "Order management API")
@Slf4j
@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService service;

    @Operation(summary = "Retrieve all orders")
    @GetMapping(produces = APPLICATION_JSON_VALUE)
    Collection<OrderDTO> getAll() {
        return service.getAll();
    }

    @Operation(summary = "Retrieve order by ID")
    @GetMapping(path ="/{id}", produces = APPLICATION_JSON_VALUE)
    OrderDTO getById(@PathVariable Integer id) {
        return service.getById(id);
    }

    @Operation(summary = "Retrieve order history by ID")
    @GetMapping(path = "/{id}/history")
    Collection<OrderHistoryDTO> getHistoryById(@PathVariable Integer id) {
        return service.getHistoryById(id);
    }
}
