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
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProducerServiceImpl implements ProducerService {

    private final ProducerRepository producerRepo;
    private final ProducerMapper mapper;

    @Override
    public List<ProducerDTO> getAll() {
        return producerRepo.findAll().stream()
                .map(mapper::toDTO)
                .toList();
    }

    @Override
    public ProducerDTO getById(Integer id) {
        return producerRepo.findById(id)
                .map(mapper::toDTO)
                .orElseThrow(() -> new ResourceNotFoundException(String.format(
                        "Producer with ID %d not found", id))
                );
    }

    @Override
    public ProducerDTO getByName(String name) {
        return producerRepo.findByName(name)
                .map(mapper::toDTO)
                .orElseThrow(() -> new ResourceNotFoundException(String.format(
                        "Producer with name %s not found", name))
                );
    }

    @Override
    public ProducerDTO create(ProducerDTO dto, MultipartFile logo) {
        var producer = mapper.toEntity(dto);

        //TODO Create better way to generate link to file
        var fileName = logo.getOriginalFilename();
        var fileUrl = fileName + "-" + UUID.randomUUID();
        producer.setLogoUrl(fileUrl);

        log.info("Saving producer to db: ");
        return mapper.toDTO(producerRepo.save(producer));
    }

    @Override
    public ProducerDTO update(Integer id, ProducerDTO dto, MultipartFile logo) {
        var producer = producerRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(String.format(
                        "Producer with id %d not found", id))
                );

        mapper.update(dto, producer);
        //TODO add file saving
        var fileName = logo.getOriginalFilename();
        var fileUrl = fileName + "-" + UUID.randomUUID();

        producer.setLogoUrl(fileUrl);
        log.info("Updating producer in db: ");
        return mapper.toDTO(producerRepo.save(producer));
    }

    @Override
    public void delete(Integer id) {
        log.info("Delete producer with id: {}", id);
        producerRepo.deleteById(id);
    }
}
