package ua.com.agroswit.productservice.service.iml;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import ua.com.agroswit.productservice.dto.mapper.ProducerMapper;
import ua.com.agroswit.productservice.dto.ProducerDTO;
import ua.com.agroswit.productservice.dto.response.SimpleDetailedProductDTO;
import ua.com.agroswit.productservice.exceptions.ResourceInConflictStateException;
import ua.com.agroswit.productservice.exceptions.ResourceNotFoundException;
import ua.com.agroswit.productservice.repository.ProducerRepository;
import ua.com.agroswit.productservice.service.ProducerService;
import ua.com.agroswit.productservice.service.ProductService;

import java.util.List;


@Slf4j
@Service
@RequiredArgsConstructor
public class ProducerServiceImpl implements ProducerService {

    private final MinioUploadService uploadService;
    private final ProductService productService;
    private final ProducerRepository producerRepo;
    private final ProducerMapper mapper;


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
                .orElseThrow(() -> new ResourceNotFoundException("Producer with id %d not found".formatted(id)));
    }

    @Override
    @Transactional(readOnly = true)
    public ProducerDTO getByName(String name) {
        return producerRepo.findByNameIgnoreCase(name)
                .map(p -> mapper.toDTO(p, uploadService.getUrl(p.getLogo())))
                .orElseThrow(() -> new ResourceNotFoundException("Producer with name %s not found".formatted(name)));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<SimpleDetailedProductDTO> getAllByProductsById(Integer id, Pageable pageable) {
        if (!producerRepo.existsById(id)) {
            throw new ResourceNotFoundException("Producer with id %d not found".formatted(id));
        }
        return productService.getAllDetailedByProducerId(id, pageable);
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    public ProducerDTO create(ProducerDTO dto) {
        if (producerRepo.existsByName(dto.name())) {
            throw new ResourceInConflictStateException("Producer with name %s already exists".formatted(dto.name()));
        }

        var producer = mapper.toEntity(dto);

        log.info("Saving producer to db: ");
        var savedProducer = producerRepo.save(producer);
        return mapper.toDTO(savedProducer, uploadService.getUrl(savedProducer.getLogo()));
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    public String saveLogoById(Integer producerId, MultipartFile logo) {
        var producer = producerRepo.findById(producerId)
                .orElseThrow(() -> new ResourceNotFoundException("Producer with id %d not found".formatted(producerId)));

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
    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    public ProducerDTO update(Integer id, ProducerDTO dto) {
        var producer = producerRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Producer with id %d not found".formatted(id)));

        if (producer.getName() != null && !producer.getName().equals(dto.name())
                && producerRepo.existsByName(dto.name())) {
            throw new ResourceInConflictStateException(
                    "Producer with name %s already exists".formatted(dto.name()));
        }

        mapper.update(dto, producer);

        log.info("Updating producer in db: {}", producer);
        var savedProducer = producerRepo.save(producer);
        return mapper.toDTO(savedProducer, uploadService.getUrl(savedProducer.getLogo()));
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
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
    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    public void removeLogoById(Integer id) {
        var producer = producerRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Producer with id %d not found".formatted(id)));

        if (producer.getLogo() != null) {
            log.trace("Deleting producer logo from storage: {}", producer.getLogo());
            uploadService.remove(producer.getLogo());

            log.info("Updating producer in db without logo: {}", producer);
            producer.setLogo(null);
            producerRepo.save(producer);
        }
    }
}
