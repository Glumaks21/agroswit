package ua.com.agroswit.inventoryservice.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import ua.com.agroswit.inventoryservice.model.Inventory;

@Repository
public interface InventoryRepository extends ReactiveCrudRepository<Inventory, Integer> {
    Flux<Inventory> findAllBy(Pageable pageable);
}
