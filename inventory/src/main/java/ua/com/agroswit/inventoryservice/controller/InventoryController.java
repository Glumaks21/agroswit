package ua.com.agroswit.inventoryservice.controller;

import com.fasterxml.jackson.annotation.JsonView;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.headers.Header;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ua.com.agroswit.inventoryservice.dto.Groups;
import ua.com.agroswit.inventoryservice.dto.InventoryDTO;
import ua.com.agroswit.inventoryservice.dto.InventoryDetailedDTO;
import ua.com.agroswit.inventoryservice.dto.Views;
import ua.com.agroswit.inventoryservice.service.InventoryService;

import java.util.List;
import java.util.Optional;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;


@Tag(name = "Inventory management API")
@Slf4j
@RestController
@RequestMapping("/api/v1/inventory")
@RequiredArgsConstructor
public class InventoryController {

    private final InventoryService service;


    @Operation(summary = "Retrieve inventory records")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    headers = @Header(name = "X-Total-Count", description = "Total count of elements")
            ),
    })
    @GetMapping(produces = APPLICATION_JSON_VALUE)
    Mono<ResponseEntity<List<InventoryDTO>>> getAll(@RequestParam(defaultValue = "1") int page,
                                                    @RequestParam(defaultValue = "10") int size,
                                                    @RequestParam Optional<Boolean> stock) {
        var pageable = page >= 1 && size > 0
                ? PageRequest.of(page - 1, size)
                : PageRequest.of(0, 10);

        return stock
                .map(s -> s
                        ? service.getAllInStock(pageable)
                        : service.getAllOutOfStock(pageable)
                )
                .orElseGet(() -> service.getAll(pageable))
                .map(inventoryPage -> {
                    var headers = new HttpHeaders();
                    headers.add("X-Total-Count", String.valueOf(inventoryPage.getTotalElements()));

                    return new ResponseEntity<>(inventoryPage.getContent(), headers, OK);
                });
    }

    @Operation(summary = "Retrieve inventory record by 1C ID")
    @GetMapping(path = "/{id}", produces = APPLICATION_JSON_VALUE)
    Mono<InventoryDTO> getById(@PathVariable Integer id) {
        return service.getById(id);
    }

    @GetMapping(params = "p_id", produces = APPLICATION_JSON_VALUE)
    Flux<InventoryDTO> getByProductIds(@RequestParam(name = "p_id") List<Integer> ids) {
        return service.getAllByProductIds(ids);
    }

    @Operation(summary = "Create new inventory record")
    @ResponseStatus(CREATED)
    @PostMapping(consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    Mono<InventoryDTO> save(@Validated(Groups.Create.class)
                                    @JsonView(Views.Create.class)
                                    @RequestBody InventoryDTO dto) {
        log.info("Received inventory creating request: {}", dto);
        return service.save(dto);
    }

    @Operation(summary = "Update of inventory record")
    @PutMapping(path = "/{id}", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    Mono<InventoryDTO> update(@PathVariable Integer id,
                                      @Validated(Groups.Update.class)
                                      @JsonView(Views.Update.class)
                                      @RequestBody InventoryDTO dto) {
        log.info("Received inventory updating request with 1C id {} and dto {}", id, dto);
        return service.updateById(dto, id);
    }

    @Operation(summary = "Delete inventory record by 1C ID")
    @DeleteMapping(path = "/{id}")
    Mono<Void> deleteById(@PathVariable Integer id) {
        log.info("Received inventory deleting request with 1C id: {}", id);
        return service.deleteById(id);
    }
}