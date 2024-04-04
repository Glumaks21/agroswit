package ua.com.agroswit.inventoryservice;

import java.util.Collection;
import java.util.List;

public interface InventoryService {
    List<InventoryDTO> getAll();
    InventoryDTO getById(Integer id);
    List<InventoryDTO> getAllByIds(Collection<Integer> ids);
    InventoryDTO save(InventoryDTO dto);
    InventoryDTO updateById(InventoryDTO dto, Integer id);
    void delete(Integer id);
}
