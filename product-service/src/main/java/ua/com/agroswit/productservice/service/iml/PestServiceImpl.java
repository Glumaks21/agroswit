package ua.com.agroswit.productservice.service.iml;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
    @Transactional
    public PestDTO create(PestDTO dto) {
        if (pestRepo.existsByName(dto.getName())) {
            throw new ResourceInConflictStateException(String.format(
                    "Pest with name %s already exists", dto.getName())
            );
        }

        var pest = mapper.toEntity(dto);
        log.info("Saving pest to db: {}", pest);
        return mapper.toDTO(pestRepo.save(pest));
    }

    @Override
    @Transactional
    public void deleteById(Integer id) {
        log.info("Deleting pest with id from db: {}", id);
        pestRepo.deleteById(id);
    }
}
