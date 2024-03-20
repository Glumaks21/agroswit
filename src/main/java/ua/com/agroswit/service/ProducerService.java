package ua.com.agroswit.service;

import ua.com.agroswit.model.Producer;

import java.util.List;
import java.util.Optional;

public interface ProducerService {
    List<Producer> getAll();
    Optional<Producer> getById(Integer id);
    Optional<Producer> getByName(String name);
    Producer create();
    void delete(Integer id);
}
