package ua.com.agroswit.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ua.com.agroswit.model.Producer;
import ua.com.agroswit.service.ProducerService;

import java.util.Collection;

@RestController
@RequestMapping("/api/v1/producers")
@RequiredArgsConstructor
public class ProducerController {

    private final ProducerService service;

    @GetMapping
    Collection<Producer> getAll() {
        return service.getAll();
    }
}
