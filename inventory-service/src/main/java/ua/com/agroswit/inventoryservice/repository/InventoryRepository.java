package ua.com.agroswit.inventoryservice.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Modifying;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ua.com.agroswit.inventoryservice.model.Inventory;

import java.util.Collection;

@Repository
public interface InventoryRepository extends ReactiveCrudRepository<Inventory, Integer> {

    Mono<Boolean> existsByProductId(Integer productId);

    @Query("SELECT count(*) FROM inventory WHERE quantity > 0")
    Mono<Long> countInStock();

    @Query("SELECT count(*) FROM inventory WHERE quantity = 0")
    Mono<Long> countOutOfStock();

    Flux<Inventory> findAllBy(Pageable pageable);

    @Query("SELECT * FROM inventory WHERE quantity > 0")
    Flux<Inventory> findAllInStock(Pageable pageable);

    @Query("SELECT * FROM inventory WHERE quantity = 0")
    Flux<Inventory> findAllOutOfStock(Pageable pageable);

    Flux<Inventory> findAllByProductIdIn(Collection<Integer> productIds);

}
