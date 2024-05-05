package ua.com.agroswit.inventoryservice.config;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.GetExchange;
import reactor.core.publisher.Flux;
import ua.com.agroswit.inventoryservice.dto.CatalogServiceProductDTO;

import java.util.Collection;

public interface CatalogClient {

    @GetExchange("/api/v1/products")
    Flux<CatalogServiceProductDTO> getAllByIds(@RequestParam(name = "id") Collection<Integer> ids);

}
