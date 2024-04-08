package ua.com.agroswit.inventoryservice.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ua.com.agroswit.inventoryservice.dto.InventoryDTO;

import java.util.Collection;
import java.util.List;

public interface InventoryService {
    Page<InventoryDTO> getAll(Pageable pageable);
    InventoryDTO getById(Integer id);
    List<InventoryDTO> getByIds(Collection<Integer> ids);
    InventoryDTO save(InventoryDTO dto);
    InventoryDTO updateById(InventoryDTO dto, Integer id);
    void delete(Integer id);
}
