package ua.com.agroswit.orderservice.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ua.com.agroswit.orderservice.dto.OrderDTO;
import ua.com.agroswit.orderservice.dto.OrderHistoryDTO;
import ua.com.agroswit.orderservice.service.OrderService;

import java.util.Collection;
import java.util.Set;

import static org.springframework.http.HttpStatus.OK;
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
    ResponseEntity<Collection<OrderDTO>> getAll(@PageableDefault Pageable pageable) {
        var page = service.getAll(pageable);
        var headers = new HttpHeaders();
        headers.add("X-Total-Count", String.valueOf(page.getTotalElements()));
        return new ResponseEntity<>(page.getContent(), headers, OK);
    }

    @Operation(summary = "Retrieve order by ID")
    @GetMapping(path ="/{id}", produces = APPLICATION_JSON_VALUE)
    OrderDTO getById(@PathVariable Integer id) {
        return service.getById(id);
    }

    @GetMapping(params = "id", produces = APPLICATION_JSON_VALUE)
    Collection<OrderDTO> getByIds(@RequestParam(name = "id") Set<Integer> ids) {
        return service.getByIds(ids);
    }

    @Operation(summary = "Create an order")
    @PostMapping(consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    OrderDTO create(@RequestBody OrderDTO dto) {
        log.info("Received order creation request with dto: {}", dto);
        return service.create(dto);
    }

    @Operation(summary = "Retrieve order history by ID")
    @GetMapping(path = "/{id}/history")
    Collection<OrderHistoryDTO> getHistoryById(@PathVariable Integer id) {
        return service.getHistoryById(id);
    }
}
