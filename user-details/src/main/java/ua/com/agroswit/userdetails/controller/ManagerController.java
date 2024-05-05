package ua.com.agroswit.userdetails.controller;

import com.fasterxml.jackson.annotation.JsonView;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.sql.Update;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ua.com.agroswit.userdetails.dto.OrderServiceDTO;
import ua.com.agroswit.userdetails.dto.request.ManagerModifyingDTO;
import ua.com.agroswit.userdetails.dto.response.ManagerDTO;
import ua.com.agroswit.userdetails.dto.view.Views;
import ua.com.agroswit.userdetails.dto.view.Views.CreateView;
import ua.com.agroswit.userdetails.dto.view.Views.UpdateView;
import ua.com.agroswit.userdetails.model.enums.UkrainianDistrict;
import ua.com.agroswit.userdetails.service.ManagerService;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Tag(name = "Manager details management API")
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/v1/managers")
@RestController
public class ManagerController {

    private final ManagerService service;


    @Operation(summary = "Retrieve all managers details",
            parameters = @Parameter(
                    name = "district", description = "Filter managers by district",
                    schema = @Schema(implementation = UkrainianDistrict.class)))
    @GetMapping(produces = APPLICATION_JSON_VALUE)
    List<ManagerDTO> getAll(@RequestParam Optional<UkrainianDistrict> district) {
        return district.map(service::getAllByDistrict).orElseGet(service::getAll);
    }

    @Operation(summary = "Retrieve manager details by ID")
    @GetMapping(path = "/{id}", produces = APPLICATION_JSON_VALUE)
    ManagerDTO getById(@PathVariable Integer id) {
        return service.getById(id);
    }

    @Operation(summary = "Retrieve orders by manager ID")
    @GetMapping(path = "/{id}/orders", produces = APPLICATION_JSON_VALUE)
    Collection<OrderServiceDTO> getOrders(@PathVariable Integer id) {
        return service.getAllOrdersById(id);
    }

    @Operation(summary = "Create a manager details")
    @ResponseStatus(CREATED)
    @PostMapping(consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    ManagerDTO create(@RequestBody @JsonView(CreateView.class) ManagerModifyingDTO dto) {
        log.info("Received manager creation request with dto: {}", dto);
        return service.create(dto);
    }


    @Operation(summary = "Update manager details by ID")
    @PutMapping(path = "/{id}", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    ManagerDTO update(@PathVariable Integer id, @RequestBody @JsonView(UpdateView.class) ManagerModifyingDTO dto) {
        log.info("Received manager updating request for id {} and dto: {}", id, dto);
        return service.update(id, dto);
    }

    @Operation(summary = "Delete manager details by ID")
    @DeleteMapping("/{id}")
    void deleteById(@PathVariable Integer id) {
        log.info("Received manager deleting request for id: {}", id);
        service.deleteById(id);
    }
}
