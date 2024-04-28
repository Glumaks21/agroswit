package ua.com.agroswit.productservice.service.iml;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.com.agroswit.productservice.dto.mapper.PestMapper;
import ua.com.agroswit.productservice.dto.PestDTO;
import ua.com.agroswit.productservice.exceptions.ResourceInConflictStateException;
import ua.com.agroswit.productservice.repository.PestRepository;
import ua.com.agroswit.productservice.service.PestService;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class PestServiceImpl implements PestService {

    private final PestRepository pestRepo;
    private final PestMapper mapper;

    @Override
    @Transactional(readOnly = true)
    public List<PestDTO> getAll() {
        return pestRepo.findAll().stream()
                .map(mapper::toDTO)
                .toList();
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    public PestDTO create(PestDTO dto) {
        if (pestRepo.existsByName(dto.name())) {
            throw new ResourceInConflictStateException(String.format(
                    "Pest with name %s already exists", dto.name())
            );
        }

        var pest = mapper.toEntity(dto);
        log.info("Saving pest to db: {}", pest);
        return mapper.toDTO(pestRepo.save(pest));
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    public void deleteById(Integer id) {
        log.info("Deleting pest with id from db: {}", id);
        pestRepo.deleteById(id);
    }
}
