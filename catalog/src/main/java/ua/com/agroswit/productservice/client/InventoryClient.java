package ua.com.agroswit.productservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ua.com.agroswit.productservice.dto.response.InventoryServiceDTO;

import java.util.Collection;
import java.util.List;

@FeignClient("inventory-service")
public interface InventoryClient {

    @GetMapping("/api/v1/inventory")
    List<InventoryServiceDTO> getByProductIds(@RequestParam(name = "p_id") Collection<Integer> ids);

}
