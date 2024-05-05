package ua.com.agroswit.inventoryservice.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ua.com.agroswit.inventoryservice.dto.InventoryDTO;
import ua.com.agroswit.inventoryservice.dto.InventoryDetailedDTO;

import java.util.Collection;

public interface InventoryService {
    Mono<Page<InventoryDTO>> getAll(Pageable pageable);
    Mono<Page<InventoryDTO>> getAllInStock(Pageable pageable);
    Mono<Page<InventoryDTO>> getAllOutOfStock(Pageable pageable);
    Mono<Page<InventoryDetailedDTO>> getAllDetailed(Pageable pageable);
    Mono<Page<InventoryDetailedDTO>> getAllDetailedInStock(Pageable pageable);
    Mono<Page<InventoryDetailedDTO>> getAllDetailedOutOfStock(Pageable pageable);
    Mono<InventoryDTO> getById(Integer id);
    Flux<InventoryDTO> getAllByProductIds(Collection<Integer> ids);
    Mono<InventoryDTO> save(InventoryDTO dto);
    Mono<InventoryDTO> updateById(InventoryDTO dto, Integer id);
    Mono<Void> deleteById(Integer id);
}
