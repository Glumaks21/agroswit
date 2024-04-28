package ua.com.agroswit.productservice.service.iml;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.com.agroswit.productservice.dto.mapper.FilterMapper;
import ua.com.agroswit.productservice.dto.FilterDTO;
import ua.com.agroswit.productservice.exceptions.ResourceInConflictStateException;
import ua.com.agroswit.productservice.exceptions.ResourceNotFoundException;
import ua.com.agroswit.productservice.model.Filter;
import ua.com.agroswit.productservice.repository.FilterRepository;
import ua.com.agroswit.productservice.service.FilterService;

import java.util.Collection;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class FilterServiceImpl implements FilterService {

    private final FilterRepository filterRepo;
    private final FilterMapper mapper;


    @Override
    @Transactional(readOnly = true)
    public List<FilterDTO> getAllHighLevel() {
        return mapper.toDTOs(filterRepo.findAllByParentFilterNull());
    }

    @Override
    @Transactional(readOnly = true)
    public List<FilterDTO> getAllByIds(Collection<Integer> ids) {
        return mapper.toDTOs(filterRepo.findAllById(ids));
    }

    @Override
    @Transactional(readOnly = true)
    public FilterDTO getById(Integer id) {
        return filterRepo.findById(id)
                .map(mapper::toDTO)
                .orElseThrow(() -> new ResourceNotFoundException(String.format(
                        "Filter with id %d not found", id))
                );
    }


    @Override
    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    public FilterDTO create(FilterDTO dto) {
        if (filterRepo.existsByName(dto.name())) {
            throw new ResourceInConflictStateException(String.format(
                    "Filter with name %s already exists", dto.name())
            );
        }

        Filter parentFilter = null;
        if (dto.parentFilterId() != null) {
            parentFilter = filterRepo.findById(dto.parentFilterId())
                    .orElseThrow(() -> new ResourceNotFoundException(String.format(
                            "Parent filter with id %d not found", dto.parentFilterId()))
                    );
        }

        var filter = mapper.toEntity(dto);
        filter.setParentFilter(parentFilter);

        log.info("Saving filter to db: {}", filter);
        return mapper.toDTO(filterRepo.save(filter));
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    public FilterDTO update(Integer id, FilterDTO dto) {
        if (id.equals(dto.parentFilterId())) {
            throw new IllegalArgumentException(
                    "Filter cannot have parent filter as itself"
            );
        }

        var filter = filterRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(String.format(
                        "Filter with id %d not found", id))
                );

        if (filter.getName() != null && !filter.getName().equals(dto.name())
                && filterRepo.existsByName(dto.name())) {
            throw new ResourceInConflictStateException(String.format(
                    "Filter with name %s already exists", dto.name())
            );
        }

        var parentFilter = filter.getParentFilter();
        if (dto.parentFilterId() != null) {
            if (parentFilter == null || !parentFilter.getId().equals(dto.parentFilterId())) {
                parentFilter = filterRepo.findById(dto.parentFilterId())
                        .orElseThrow(() -> new ResourceNotFoundException(String.format(
                                "Parent filter with id %d not found", dto.parentFilterId()))
                        );
            }
        } else {
            parentFilter = null;
        }

        mapper.update(dto, filter);
        filter.setParentFilter(parentFilter);

        log.info("Updating filter to db: {}", filter);
        return mapper.toDTO(filterRepo.save(filter));
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    public void deleteById(Integer id) {
        log.info("Deleting filter with id {} from db", id);
        filterRepo.deleteById(id);
    }
}
