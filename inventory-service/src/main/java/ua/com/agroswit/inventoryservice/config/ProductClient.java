package ua.com.agroswit.inventoryservice.config;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.GetExchange;
import reactor.core.publisher.Flux;
import ua.com.agroswit.inventoryservice.dto.ProductServiceProductDTO;

import java.util.Collection;

public interface ProductClient {

    @GetExchange("/api/v1/products")
    Flux<ProductServiceProductDTO> getAllByIds(@RequestParam(name = "id") Collection<Integer> ids);

}
