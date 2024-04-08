package ua.com.agroswit.inventoryservice.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ua.com.agroswit.inventoryservice.dto.InventoryDTO;

import java.util.Collection;
import java.util.List;

public interface InventoryService {
    Mono<Page<InventoryDTO>> getAll(Pageable pageable);
    Flux<InventoryDTO> getByIds(Collection<Integer> ids);
    Mono<InventoryDTO> getById(Integer id);
    Mono<InventoryDTO> save(InventoryDTO dto);
    Mono<InventoryDTO> updateById(InventoryDTO dto, Integer id);
    Mono<Void> delete(Integer id);
}
