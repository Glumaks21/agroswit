package ua.com.agroswit.productservice.service.iml;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import ua.com.agroswit.productservice.dto.mapper.ProducerMapper;
import ua.com.agroswit.productservice.dto.ProducerDTO;
import ua.com.agroswit.productservice.exceptions.ResourceInConflictStateException;
import ua.com.agroswit.productservice.exceptions.ResourceNotFoundException;
import ua.com.agroswit.productservice.repository.ProducerRepository;
import ua.com.agroswit.productservice.service.ProducerService;

import java.util.List;


@Slf4j
@Service
@RequiredArgsConstructor
public class ProducerServiceImpl implements ProducerService {

    private final ProducerRepository producerRepo;
    private final ProducerMapper mapper;
    private final MinioUploadService uploadService;


    @Override
    @Transactional(readOnly = true)
    public List<ProducerDTO> getAll() {
        return producerRepo.findAll().stream()
                .map(p -> mapper.toDTO(p, uploadService.getUrl(p.getLogo())))
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public ProducerDTO getById(Integer id) {
        return producerRepo.findById(id)
                .map(p -> mapper.toDTO(p, uploadService.getUrl(p.getLogo())))
                .orElseThrow(() -> new ResourceNotFoundException(String.format(
                        "Producer with id %d not found", id))
                );
    }

    @Override
    @Transactional(readOnly = true)
    public ProducerDTO getByName(String name) {
        return producerRepo.findByName(name)
                .map(p -> mapper.toDTO(p, uploadService.getUrl(p.getLogo())))
                .orElseThrow(() -> new ResourceNotFoundException(String.format(
                        "Producer with name %s not found", name))
                );
    }

    @Override
    @Transactional
    public ProducerDTO create(ProducerDTO dto) {
        if (producerRepo.existsByName(dto.name())) {
            throw new ResourceInConflictStateException(String.format(
                    "Producer with name %s already exists", dto.name())
            );
        }

        var producer = mapper.toEntity(dto);

        log.info("Saving producer to db: ");
        var savedProducer = producerRepo.save(producer);
        return mapper.toDTO(savedProducer, uploadService.getUrl(savedProducer.getLogo()));
    }

    @Override
    @Transactional
    public String saveLogoById(Integer producerId, MultipartFile logo) {
        var producer = producerRepo.findById(producerId)
                .orElseThrow(() -> new ResourceNotFoundException(String.format(
                        "Producer with id %d not found", producerId))
                );

        log.trace("Saving producer logo to storage: {}", logo.getOriginalFilename());
        var logoName = uploadService.uploadImage(logo);

        if (producer.getLogo() != null) {
            log.trace("Deleting producer previous logo from storage");
            uploadService.remove(producer.getLogo());
        }

        producer.setLogo(logoName);
        log.info("Updating producer with new logo in db: {}", producer);
        producerRepo.save(producer);
        return uploadService.getUrl(logoName);
    }

    @Override
    @Transactional
    public ProducerDTO update(Integer id, ProducerDTO dto) {
        var producer = producerRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(String.format(
                        "Producer with id %d not found", id))
                );

        if (producer.getName() != null && !producer.getName().equals(dto.name())
                && producerRepo.existsByName(dto.name())) {
            throw new ResourceInConflictStateException(String.format(
                    "Producer with name %s already exists", dto.name())
            );
        }

        mapper.update(dto, producer);

        log.info("Updating producer in db: {}", producer);
        var savedProducer = producerRepo.save(producer);
        return mapper.toDTO(savedProducer, uploadService.getUrl(savedProducer.getLogo()));
    }

    @Override
    @Transactional
    public void deleteById(Integer id) {
        producerRepo.findById(id)
                .ifPresent(p -> {
                    if (p.getLogo() != null) {
                        log.trace("Deleting producer logo from storage: {}", p.getLogo());
                        uploadService.remove(p.getLogo());
                    }

                    log.info("Delete producer from db with id: {}", id);
                    producerRepo.deleteById(id);
                });
    }

    @Override
    @Transactional
    public void removeLogoById(Integer id) {
        var producer = producerRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(String.format(
                        "Producer with id %d not found", id))
                );

        if (producer.getLogo() != null) {
            log.trace("Deleting producer logo from storage: {}", producer.getLogo());
            uploadService.remove(producer.getLogo());

            log.info("Updating producer in db without logo: {}", producer);
            producer.setLogo(null);
            producerRepo.save(producer);
        }
    }
}
