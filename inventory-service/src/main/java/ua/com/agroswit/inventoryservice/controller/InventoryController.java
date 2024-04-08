package ua.com.agroswit.inventoryservice.controller;

import com.fasterxml.jackson.annotation.JsonView;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.headers.Header;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ua.com.agroswit.inventoryservice.dto.Groups;
import ua.com.agroswit.inventoryservice.dto.InventoryDTO;
import ua.com.agroswit.inventoryservice.dto.Views;
import ua.com.agroswit.inventoryservice.service.InventoryService;

import java.util.List;
import java.util.Optional;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;


@Tag(name = "Inventory management API")
@Slf4j
@RestController
@RequestMapping("/api/v1/inventory")
@RequiredArgsConstructor
public class InventoryController {

    private final InventoryService service;

    @Operation(summary = "Retrieve products in inventory")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    headers = @Header(description = "Total count of elements, when id param is not specified",
                            name = "X-Total-Count"
                    )
            ),
    })
    @JsonView(Views.Public.class)
    @GetMapping(produces = APPLICATION_JSON_VALUE)
    ResponseEntity<List<InventoryDTO>> getAll(@ParameterObject @PageableDefault Pageable pageable,
                                              @RequestParam(name = "id") Optional<List<Integer>> ids) {
        if (ids.isPresent()) {
            return ResponseEntity.ok(service.getByIds(ids.get()));
        }

        var page = service.getAll(pageable);
        var headers = new HttpHeaders();
        headers.add("X-Total-Count", String.valueOf(page.getTotalElements()));

        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    @Operation(summary = "Retrieve product by 1С ID")
    @JsonView(Views.Public.class)
    @GetMapping(path = "/{id}", produces = APPLICATION_JSON_VALUE)
    InventoryDTO getById(@PathVariable Integer id) {
        return service.getById(id);
    }

    @Operation(summary = "Save new product")
    @JsonView(Views.Public.class)
    @ResponseStatus(CREATED)
    @PostMapping(consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    InventoryDTO save(@Validated(Groups.Create.class)
                      @JsonView(Views.Create.class)
                      @RequestBody InventoryDTO dto) {
        log.info("Received product saving request: {}", dto);
        return service.save(dto);
    }

    @Operation(summary = "Partial update of saved product by 1С ID")
    @JsonView(Views.Public.class)
    @PatchMapping(path = "/{id}", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    InventoryDTO update(@PathVariable Integer id,
                        @Validated(Groups.Update.class)
                        @JsonView(Views.Update.class)
                        @RequestBody InventoryDTO dto) {
        log.info("Received product updating request with 1c id {} and dto {}", id, dto);
        return service.updateById(dto, id);
    }

    @Operation(summary = "Delete product from inventory by 1C ID")
    @JsonView(Views.Public.class)
    @DeleteMapping(path = "/{id}")
    void deleteById(@PathVariable Integer id) {
        log.info("Received product deleting request with 1c id: {}", id);
        service.delete(id);
    }
}