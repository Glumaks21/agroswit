package ua.com.agroswit.userdetails.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import ua.com.agroswit.userdetails.dto.response.UserServiceDTO;

import java.util.List;
import java.util.Set;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;


@FeignClient("auth-server")
public interface UserClient {
    @GetMapping(path = "/api/v1/users/{id}", produces = APPLICATION_JSON_VALUE)
    UserServiceDTO getById(@PathVariable Integer id);

    @GetMapping(path = "/api/v1/users", produces = APPLICATION_JSON_VALUE)
    List<UserServiceDTO> getByIds(@RequestParam(name = "id") Set<Integer> ids);
}
