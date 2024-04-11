package ua.com.agroswit.productservice.service.iml;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.InputStreamResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.RestClient;
import org.springframework.web.multipart.MultipartFile;
import ua.com.agroswit.productservice.dto.ProducerDTO;
import ua.com.agroswit.productservice.dto.mapper.ProducerMapper;
import ua.com.agroswit.productservice.exceptions.ResourceInConflictStateException;
import ua.com.agroswit.productservice.exceptions.ResourceNotFoundException;
import ua.com.agroswit.productservice.repository.ProducerRepository;
import ua.com.agroswit.productservice.service.ProducerService;
import java.util.List;

import static org.springframework.http.MediaType.MULTIPART_FORM_DATA;

@Slf4j
@Service
public class ProducerServiceImpl implements ProducerService {

    private final ProducerRepository producerRepo;
    private final ProducerMapper mapper;
    private final RestClient restClient;

    public ProducerServiceImpl(ProducerRepository producerRepo,
                               ProducerMapper mapper,
                               RestClient.Builder restClientBuilder) {
        this.producerRepo = producerRepo;
        this.mapper = mapper;
        this.restClient = restClientBuilder.build();
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProducerDTO> getAll() {
        return producerRepo.findAll().stream()
                .map(mapper::toDTO)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public ProducerDTO getById(Integer id) {
        return producerRepo.findById(id)
                .map(mapper::toDTO)
                .orElseThrow(() -> new ResourceNotFoundException(String.format(
                        "Producer with id %d not found", id))
                );
    }

    @Override
    @Transactional(readOnly = true)
    public ProducerDTO getByName(String name) {
        return producerRepo.findByName(name)
                .map(mapper::toDTO)
                .orElseThrow(() -> new ResourceNotFoundException(String.format(
                        "Producer with name %s not found", name))
                );
    }

    @SneakyThrows
    @Override
    @Transactional
    public ProducerDTO create(ProducerDTO dto, MultipartFile logo) {
        if (producerRepo.existsByName(dto.name())) {
            throw new ResourceInConflictStateException(String.format(
                    "Producer with name %s already exists", dto.name())
            );
        }
        var producer = mapper.toEntity(dto);

        log.info("Request storage service");
        var multiValueMap = new LinkedMultiValueMap<>();
        multiValueMap.add("file", new InputStreamResource(logo.getInputStream()));
        var response = restClient.post()
                .uri("http://storage-service/api/v1/uploads")
                .contentType(MULTIPART_FORM_DATA)
                .body(multiValueMap)
                .retrieve()
                .toBodilessEntity();
        var logoUrl = response.getHeaders().get("Location").getFirst();
        producer.setLogoUrl(logoUrl);

        log.info("Saving producer to db: ");
        return mapper.toDTO(producerRepo.save(producer));
    }

    @SneakyThrows
    @Override
    @Transactional
    public ProducerDTO update(Integer id, ProducerDTO dto, MultipartFile logo) {
        var producer = producerRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(String.format(
                        "Producer with id %d not found", id))
                );

        log.info("Request deleting previous logo from storage service");
        restClient.delete()
                .uri(producer.getLogoUrl())
                .retrieve()
                .toBodilessEntity();

        mapper.update(dto, producer);

        log.info("Request saving new logo to storage service");
        var multiValueMap = new LinkedMultiValueMap<>();
        multiValueMap.add("file", new InputStreamResource(logo.getInputStream()));
        var response = restClient.post()
                .uri("http://storage-service/api/v1/uploads")
                .contentType(MULTIPART_FORM_DATA)
                .body(multiValueMap)
                .retrieve()
                .toBodilessEntity();
        var logoUrl = response.getHeaders().get("Location").getFirst();
        producer.setLogoUrl(logoUrl);

        log.info("Updating producer in db: ");
        return mapper.toDTO(producerRepo.save(producer));
    }

    @Override
    @Transactional
    public void delete(Integer id) {
        log.info("Delete producer with id: {}", id);
        producerRepo.deleteById(id);
    }
}
