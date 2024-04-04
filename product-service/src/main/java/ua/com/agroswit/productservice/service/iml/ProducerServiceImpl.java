package ua.com.agroswit.productservice.service.iml;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ua.com.agroswit.productservice.dto.ProducerDTO;
import ua.com.agroswit.productservice.dto.mapper.ProducerMapper;
import ua.com.agroswit.productservice.exceptions.ResourceNotFoundException;
import ua.com.agroswit.productservice.repository.ProducerRepository;
import ua.com.agroswit.productservice.service.ProducerService;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProducerServiceImpl implements ProducerService {

    private final ProducerRepository repository;
    //    private final FileStorageService storageService;
    private final ProducerMapper mapper;

    @Override
    public List<ProducerDTO> getAll() {
        return repository.findAll().stream()
                .map(mapper::toDTO)
                .toList();
    }

    @Override
    public ProducerDTO getById(Integer id) {
        return repository.findById(id)
                .map(mapper::toDTO)
                .orElseThrow(() -> new ResourceNotFoundException(String.format(
                        "Producer with ID %d not found", id))
                );
    }

    @Override
    public ProducerDTO getByName(String name) {
        return repository.findByName(name)
                .map(mapper::toDTO)
                .orElseThrow(() -> new ResourceNotFoundException(String.format(
                        "Producer with name %s not found", name))
                );
    }

    @Override
    public ProducerDTO create(ProducerDTO dto, MultipartFile logo) {
        var producer = mapper.toEntity(dto);

//        var fileName = storageService.store(logo);
        var fileName = logo.getOriginalFilename();
        //TODO Create better way to generate link to file
        var fileUrl = "http://localhost:8080/api/v1/uploads/" + fileName;
        producer.setLogoUrl(fileUrl);

        log.info("Saving producer to db: ");
        return mapper.toDTO(repository.save(producer));
    }

    @Override
    public void delete(Integer id) {
        log.info("Delete producer with id: {}", id);
        repository.deleteById(id);
    }
}
