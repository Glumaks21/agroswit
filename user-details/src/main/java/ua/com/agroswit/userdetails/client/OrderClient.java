package ua.com.agroswit.userdetails.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ua.com.agroswit.userdetails.dto.OrderServiceDTO;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;


@FeignClient("order-service")
public interface OrderClient {
    @GetMapping(path = "/api/v1/orders", produces = APPLICATION_JSON_VALUE)
    List<OrderServiceDTO> getByIds(@RequestParam(name = "id") List<Integer> ids);
}
