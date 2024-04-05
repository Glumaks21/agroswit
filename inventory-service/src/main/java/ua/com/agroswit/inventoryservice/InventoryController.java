package ua.com.agroswit.inventoryservice;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;


@Tag(name = "Inventory management API")
@Slf4j
@RestController
@RequestMapping("/api/v1/inventory")
@RequiredArgsConstructor
public class InventoryController {

    private final InventoryService service;

    @Operation(summary = "Retrieve all products in inventory")
    @GetMapping(produces = APPLICATION_JSON_VALUE)
    Collection<InventoryDTO> getAll() {
        return service.getAll();
    }

    @Operation(summary = "Retrieve product by 1С ID")
    @GetMapping(path = "/{id}", produces = APPLICATION_JSON_VALUE)
    InventoryDTO getById(@PathVariable Integer id) {
        return service.getById(id);
    }

    @Operation(summary = "Retrieve products by many 1C IDs")
    @GetMapping(params = "id", produces = APPLICATION_JSON_VALUE)
    Collection<InventoryDTO> getByIds(@RequestParam(name = "id") Collection<Integer> ids) {
        return service.getAllByIds(ids);
    }

    @Operation(summary = "Save new product")
    @ResponseStatus(CREATED)
    @PostMapping(consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    InventoryDTO save(@RequestBody InventoryDTO dto) {
        log.info("Received product saving request: {}", dto);
        return service.save(dto);
    }

    @Operation(summary = "Partial update of saved product by 1С ID")
    @PatchMapping(path = "/{id}", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    InventoryDTO update(@PathVariable Integer id, @RequestBody InventoryDTO dto) {
        log.info("Received product updating request with 1c id {} and dto {}", id, dto);
        return service.updateById(dto, id);
    }

    @Operation(summary = "Delete product from inventory by 1C ID")
    @DeleteMapping(path = "/{id}")
    void deleteById(@PathVariable Integer id) {
        log.info("Received product deleting request with 1c id: {}", id);
        service.delete(id);
    }
}