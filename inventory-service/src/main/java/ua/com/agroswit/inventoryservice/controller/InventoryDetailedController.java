package ua.com.agroswit.inventoryservice.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;
import ua.com.agroswit.inventoryservice.dto.InventoryDetailedDTO;
import ua.com.agroswit.inventoryservice.service.InventoryService;

import java.util.List;
import java.util.Optional;

import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Tag(name = "Inventory detailed management API",
        description = "Include product details")
@RestController
@RequestMapping("/api/v1/inventory/detailed")
@RequiredArgsConstructor
public class InventoryDetailedController {

    private final InventoryService service;


    @Operation(summary = "Retrieve all inventory records")
    @GetMapping(produces = APPLICATION_JSON_VALUE)
    Mono<ResponseEntity<List<InventoryDetailedDTO>>> getAllDetailed(@RequestParam(defaultValue = "1") int page,
                                                                    @RequestParam(defaultValue = "10") int size,
                                                                    @RequestParam Optional<Boolean> stock) {
        var pageable = page >= 1 && size > 0
                ? PageRequest.of(page - 1, size)
                : PageRequest.of(0, 10);

        return stock
                .map(s -> s
                        ? service.getAllDetailedInStock(pageable)
                        : service.getAllDetailedOutOfStock(pageable)
                )
                .orElseGet(() -> service.getAllDetailed(pageable))
                .map(inventoryPage -> {
                    var headers = new HttpHeaders();
                    headers.add("X-Total-Count", String.valueOf(inventoryPage.getTotalElements()));

                    return new ResponseEntity<>(inventoryPage.getContent(), headers, OK);
                });
    }
}
