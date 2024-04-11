package ua.com.agroswit.inventoryservice.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Modifying;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ua.com.agroswit.inventoryservice.model.Inventory;

@Repository
public interface InventoryRepository extends ReactiveCrudRepository<Inventory, Integer> {

    @Modifying
    @Query("INSERT INTO inventory (product_1с_id, quantity) " +
            "VALUES (:product1CId, :quantity)")
    Mono<Integer> insert(Integer product1CId, Integer quantity);

    @Modifying
    @Query("UPDATE inventory " +
            "SET quantity = :quantity " +
            "WHERE product_1с_id = :product1CId")
    Mono<Integer> update(Integer product1CId, Integer quantity);

    Flux<Inventory> findAllBy(Pageable pageable);

}
