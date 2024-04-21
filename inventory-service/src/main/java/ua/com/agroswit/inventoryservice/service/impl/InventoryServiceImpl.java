package ua.com.agroswit.inventoryservice.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ua.com.agroswit.inventoryservice.config.ProductClient;
import ua.com.agroswit.inventoryservice.dto.InventoryDTO;
import ua.com.agroswit.inventoryservice.dto.InventoryDetailedDTO;
import ua.com.agroswit.inventoryservice.dto.ProductServiceProductDTO;
import ua.com.agroswit.inventoryservice.dto.mapper.InventoryMapper;
import ua.com.agroswit.inventoryservice.exception.ResourceInConflictStateException;
import ua.com.agroswit.inventoryservice.exception.ResourceNotFoundException;
import ua.com.agroswit.inventoryservice.model.Inventory;
import ua.com.agroswit.inventoryservice.repository.InventoryRepository;
import ua.com.agroswit.inventoryservice.service.InventoryService;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


@Slf4j
@Service
@RequiredArgsConstructor
public class InventoryServiceImpl implements InventoryService {

    private final InventoryRepository inventoryRepo;
    private final InventoryMapper mapper;
    private final ProductClient productClient;


    @Override
    @Transactional(readOnly = true)
    public Mono<Page<InventoryDTO>> getAll(Pageable pageable) {
        var inventoriesMono = inventoryRepo.findAllBy(pageable).collectList();
        var totalElementsMono = inventoryRepo.count();
        return Mono.zip(inventoriesMono, totalElementsMono)
                .map(t2 -> t2.mapT1(mapper::toDTO))
                .map(t2 -> new PageImpl<>(t2.getT1(), pageable, t2.getT2()));
    }

    @Override
    @Transactional(readOnly = true)
    public Mono<Page<InventoryDTO>> getAllInStock(Pageable pageable) {
        var inventoriesMono = inventoryRepo.findAllInStock(pageable).collectList();
        var totalElementsMono = inventoryRepo.countInStock();
        return Mono.zip(inventoriesMono, totalElementsMono)
                .map(t2 -> t2.mapT1(mapper::toDTO))
                .map(t2 -> new PageImpl<>(t2.getT1(), pageable, t2.getT2()));
    }

    @Override
    @Transactional(readOnly = true)
    public Mono<Page<InventoryDTO>> getAllOutOfStock(Pageable pageable) {
        var inventoriesMono = inventoryRepo.findAllOutOfStock(pageable).collectList();
        var totalElementsMono = inventoryRepo.countOutOfStock();
        return Mono.zip(inventoriesMono, totalElementsMono)
                .map(t2 -> t2.mapT1(mapper::toDTO))
                .map(t2 -> new PageImpl<>(t2.getT1(), pageable, t2.getT2()));
    }

    @Override
    @Transactional(readOnly = true)
    public Mono<Page<InventoryDetailedDTO>> getAllDetailed(Pageable pageable) {
        var inventoriesMono = inventoryRepo.findAllBy(pageable).collectList();
        var totalElementsMono = inventoryRepo.count();
        return Mono.zip(inventoriesMono, totalElementsMono)
                .flatMap(t2 -> {
                    var ids = t2.getT1().stream().map(Inventory::getId).toList();
                    var productRespMono = productClient.getAllByIds(ids).collectList();
                    return Mono.zip(Mono.just(t2.getT1()), productRespMono, Mono.just(t2.getT2()));
                })
                .map(t3 -> {
                    var dtos = collectToDTO(t3.getT1(), t3.getT2());
                    var totalElements = t3.getT3();
                    return new PageImpl<>(dtos, pageable, totalElements);
                });
    }

    private List<InventoryDetailedDTO> collectToDTO(List<Inventory> inventories,
                                                    List<ProductServiceProductDTO> products) {
        var dtos = new ArrayList<InventoryDetailedDTO>(inventories.size());
        for (var i : inventories) {
            for (var p : products) {
                if (i.getProductId().equals(p.id())) {
                    var dto = mapper.toDetailedDTO(i, p);
                    dtos.add(dto);
                    break;
                }
            }
        }
        return dtos;
    }

    @Override
    @Transactional(readOnly = true)
    public Mono<Page<InventoryDetailedDTO>> getAllDetailedInStock(Pageable pageable) {
        var inventoriesMono = inventoryRepo.findAllInStock(pageable).collectList();
        var totalElementsMono = inventoryRepo.countInStock();
        return Mono.zip(inventoriesMono, totalElementsMono)
                .flatMap(t2 -> {
                    var ids = t2.getT1().stream().map(Inventory::getId).toList();
                    var productRespMono = productClient.getAllByIds(ids).collectList();
                    return Mono.zip(Mono.just(t2.getT1()), productRespMono, Mono.just(t2.getT2()));
                })
                .map(t3 -> {
                    var dtos = collectToDTO(t3.getT1(), t3.getT2());
                    var totalElements = t3.getT3();
                    return new PageImpl<>(dtos, pageable, totalElements);
                });
    }

    @Override
    @Transactional(readOnly = true)
    public Mono<Page<InventoryDetailedDTO>> getAllDetailedOutOfStock(Pageable pageable) {
        var inventoriesMono = inventoryRepo.findAllOutOfStock(pageable).collectList();
        var totalElementsMono = inventoryRepo.countOutOfStock();
        return Mono.zip(inventoriesMono, totalElementsMono)
                .flatMap(t2 -> {
                    var ids = t2.getT1().stream().map(Inventory::getId).toList();
                    var productRespMono = productClient.getAllByIds(ids).collectList();
                    return Mono.zip(Mono.just(t2.getT1()), productRespMono, Mono.just(t2.getT2()));
                })
                .map(t3 -> {
                    var dtos = collectToDTO(t3.getT1(), t3.getT2());
                    var totalElements = t3.getT3();
                    return new PageImpl<>(dtos, pageable, totalElements);
                });
    }

    @Override
    @Transactional(readOnly = true)
    public Mono<InventoryDTO> getById(Integer id) {
        return inventoryRepo.findById(id)
                .map(mapper::toDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public Flux<InventoryDTO> getAllByProductIds(Collection<Integer> ids) {
        return inventoryRepo.findAllByProductIdIn(ids)
                .map(mapper::toDTO);
    }

    @Override
    @Transactional
    public Mono<InventoryDTO> save(InventoryDTO dto) {
        var inventoryProduct = mapper.toEntity(dto);
        inventoryProduct.setIsNew(true);

        var articleCheck = inventoryRepo.existsById(dto.getArticle1CId())
                .flatMap(e -> e
                        ? Mono.error(new ResourceInConflictStateException(String.format(
                        "Inventory record with 1c id %d already exists", dto.getArticle1CId())))
                        : Mono.just(false)
                );
        var productCheck = inventoryRepo.existsByProductId(dto.getProductId())
                .flatMap(e -> e
                        ? Mono.error(new ResourceInConflictStateException(String.format(
                        "Inventory record for product id %d already exists", dto.getProductId())))
                        : Mono.just(false)
                );
        var validationMono = articleCheck.then(productCheck);

        return validationMono
                .doOnNext(p -> log.info("Saving inventory db {}", inventoryProduct))
                .flatMap(p -> inventoryRepo.save(inventoryProduct)
                        .map(mapper::toDTO)
                );
    }

    @Override
    @Transactional
    public Mono<InventoryDTO> updateById(InventoryDTO dto, Integer id) {
        var inventoryProductMono = inventoryRepo.findById(id)
                .switchIfEmpty(Mono.error(new ResourceNotFoundException(String.format(
                                "Inventory record with 1c id %d not found", id))
                        )
                );

        return inventoryProductMono.flatMap(i -> {
            mapper.update(dto, i);

            log.info("Updating inventory in db: {}", i);
            return inventoryRepo.save(i)
                    .map(mapper::toDTO);
        });
    }

    @Override
    @Transactional
    public Mono<Void> deleteById(Integer id) {
        log.info("Deleting inventory record with 1c id: {}", id);
        return inventoryRepo.deleteById(id);
    }
}
