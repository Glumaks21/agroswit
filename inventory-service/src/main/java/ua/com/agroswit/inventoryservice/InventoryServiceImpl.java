package ua.com.agroswit.inventoryservice;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.Collection;
import java.util.List;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@Slf4j
@Service
@RequiredArgsConstructor
public class InventoryServiceImpl implements InventoryService {

    private final InventoryRepository inventoryRepo;
    private final InventoryMapper mapper;
    private final RestClient.Builder restClientBuilder;

    @Override
    public List<InventoryDTO> getAll() {
        return inventoryRepo.findAll().stream()
                .map(mapper::toDTO)
                .toList();
    }

    @Override
    public InventoryDTO getById(Integer id) {
        return inventoryRepo.findById(id)
                .map(mapper::toDTO)
                .orElseThrow(() -> new ResourceNotFoundException(String.format(
                        "Inventory product with 1c id %d not found", id))
                );
    }

    @Override
    public List<InventoryDTO> getAllByIds(Collection<Integer> ids) {
        return inventoryRepo.findAllByProduct1CIdIn(ids).stream()
                .map(mapper::toDTO)
                .toList();
    }

    @Override
    public InventoryDTO save(InventoryDTO dto) {
        var product = mapper.toEntity(dto);

        if (inventoryRepo.existsById(dto.product1CId())) {
            throw new ResourceInConflictStateException(String.format(
                    "Product with 1c id %d already exists", dto.product1CId())
            );
        }

        var response = restClientBuilder.build().get()
                .uri("http://product-service/api/v1/products?1c_id={id}", dto.product1CId())
                .retrieve()
                .toBodilessEntity();
        //TODO add server communication exception
        if (response.getStatusCode().isSameCodeAs(NOT_FOUND)) {
            throw new ResourceNotFoundException(String.format(
                    "Product with 1c id %d not found", dto.product1CId())
            );
        }

        log.info("Saving product to db: {}", product);
        return mapper.toDTO(inventoryRepo.save(product));
    }

    @Override
    public InventoryDTO updateById(InventoryDTO dto, Integer id) {
        var product = inventoryRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(String.format(
                        "Inventory product with 1c id %d not found", id))
                );

        mapper.update(dto, product);

        log.info("Updating product in db: {}", product);
        return mapper.toDTO(inventoryRepo.save(product));
    }

    @Override
    public void delete(Integer id) {
        log.info("Deleting product from inventory with 1c id: {}", id);
        inventoryRepo.deleteById(id);
    }
}
