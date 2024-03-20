package ua.com.agroswit.service.iml;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ua.com.agroswit.model.Producer;
import ua.com.agroswit.repository.ProducerRepository;
import ua.com.agroswit.service.ProducerService;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProducerServiceImpl implements ProducerService {

    private final ProducerRepository repository;

    @Override
    public List<Producer> getAll() {
        return repository.findAll();
    }

    @Override
    public Optional<Producer> getById(Integer id) {
        return repository.findById(id);
    }

    @Override
    public Optional<Producer> getByName(String name) {
        return repository.findByName(name);
    }

    @Override
    public Producer create() {
        return null;
    }

    @Override
    public void delete(Integer id) {
        log.info("Delete producer with id: {}", id);
        repository.deleteById(id);
    }
}
