package ua.com.agroswit.service.iml;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.com.agroswit.dto.CategoryDTO;
import ua.com.agroswit.dto.CategoryDTO.PropertyDTO;
import ua.com.agroswit.dto.mappers.CategoryMapper;
import ua.com.agroswit.exception.RequestValidationException;
import ua.com.agroswit.exception.ResourceInConflictStateException;
import ua.com.agroswit.exception.ResourceNotFoundException;
import ua.com.agroswit.model.Category;
import ua.com.agroswit.repository.CategoryRepository;
import ua.com.agroswit.service.CategoryService;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository repository;
    private final CategoryMapper mapper;

    @Override
    public List<CategoryDTO> getAllCategories() {
        return repository.findAllByParentCategoryId(null).stream()
                .map(mapper::toDTO)
                .toList();
    }

    @Override
    public CategoryDTO getById(Integer id) {
        return repository.findById(id)
                .map(mapper::toDTO)
                .orElseThrow(() -> new ResourceNotFoundException(String.format(
                        "Category with id %d not found", id))
                );
    }

    @Override
    @Transactional
    public CategoryDTO create(CategoryDTO dto) {
        validateProperties(dto.properties());

        if (repository.existsByName(dto.name())) {
            throw new ResourceInConflictStateException(String.format(
                    "Category with name %s already exists", dto.name())
            );
        }

        Category parentCategory = null;
        if (dto.parentCategoryId() != null) {
            parentCategory = repository.findById(dto.parentCategoryId())
                    .orElseThrow(() -> new ResourceNotFoundException(String.format(
                            "Parent category with id %d not found", dto.parentCategoryId()))
                    );
        }

        var category = mapper.toEntity(dto);
        category.setParentCategory(parentCategory);
        category.getProperties().forEach(p -> p.setCategory(category));

        log.info("Saving new category to db: {}", category);
        return mapper.toDTO(repository.save(category));
    }

    @Override
    @Transactional
    public CategoryDTO updateById(Integer id, CategoryDTO dto) {
        var category = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(String.format(
                        "Category with id %d not found", id))
                );

        mapper.updateFromDTO(dto, category);

        log.info("Updating category in db: {}", category);
        return mapper.toDTO(repository.save(category));
    }

    private void validateProperties(List<PropertyDTO> properties) {
        for (var i = 0; i < properties.size(); i++) {
            var prop = properties.get(i);

            for (var j = i + 1; j < properties.size(); j++) {
                var propToCompare = properties.get(j);

                if (prop.name().equals(propToCompare.name())) {
                    throw new RequestValidationException("properties", String.format(
                            "There are identical names in properties: %s", prop.name())
                    );
                }
            }
        }
    }

    @Override
    public void deleteById(Integer id) {
        log.info("Deleting category with id {} from db", id);
        repository.deleteById(id);
    }
}
