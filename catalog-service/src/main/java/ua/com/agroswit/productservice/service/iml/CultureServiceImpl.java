package ua.com.agroswit.productservice.service.iml;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.com.agroswit.productservice.dto.mapper.CultureMapper;
import ua.com.agroswit.productservice.dto.CultureDTO;
import ua.com.agroswit.productservice.exceptions.ResourceInConflictStateException;
import ua.com.agroswit.productservice.exceptions.ResourceNotFoundException;
import ua.com.agroswit.productservice.repository.CultureRepository;
import ua.com.agroswit.productservice.service.CultureService;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CultureServiceImpl implements CultureService {

    private final CultureRepository cultureRepo;
    private final CultureMapper mapper;

    @Override
    @Transactional(readOnly = true)
    public List<CultureDTO> getAll() {
        return cultureRepo.findAll().stream()
                .map(mapper::toDTO)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public CultureDTO getByName(String name) {
        return cultureRepo.findByNameIgnoreCase(name)
                .map(mapper::toDTO)
                .orElseThrow(() -> new ResourceNotFoundException(String.format(
                        "Culture with name %s not found", name))
                );
    }

    @Override
    @Transactional
    public CultureDTO create(CultureDTO dto) {
        if (cultureRepo.existsByNameIgnoreCase(dto.name())) {
            throw new ResourceInConflictStateException(String.format(
                    "Culture with name %s already exists", dto.name())
            );
        }

        var culture = mapper.toEntity(dto);
        log.info("Saving culture to db: {}", culture);
        return mapper.toDTO(cultureRepo.save(culture));
    }

    @Override
    @Transactional
    public void deleteById(Integer id) {
        log.info("Deleting culture with id from db: {}", id);
        cultureRepo.deleteById(id);
    }
}
