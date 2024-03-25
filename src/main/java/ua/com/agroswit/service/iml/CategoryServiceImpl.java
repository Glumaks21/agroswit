package ua.com.agroswit.service.iml;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.com.agroswit.dto.request.CategoryCreationDTO;
import ua.com.agroswit.dto.response.CategoryDTO;
import ua.com.agroswit.dto.response.CategoryDTO.PropertyDTO;
import ua.com.agroswit.dto.response.converter.CategoryDTOConverter;
import ua.com.agroswit.exception.RequestValidationException;
import ua.com.agroswit.exception.ResourceInConflictStateException;
import ua.com.agroswit.exception.ResourceNotFoundException;
import ua.com.agroswit.model.Category;
import ua.com.agroswit.model.CategoryProperty;
import ua.com.agroswit.repository.CategoryRepository;
import ua.com.agroswit.service.CategoryService;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository repository;
    private final CategoryDTOConverter converter;

    @Override
    public List<CategoryDTO> getAllCategories() {
        return repository.findAllByParentCategoryId(null).stream()
                .map(converter)
                .toList();
    }

    @Override
    public CategoryDTO getById(Integer id) {
        return repository.findById(id)
                .map(converter)
                .orElseThrow(() -> new ResourceNotFoundException(String.format(
                        "Category with id %d not found", id))
                );
    }

    @Transactional
    @Override
    public CategoryDTO create(CategoryCreationDTO dto) {
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

        var category = new Category();

        var properties = dto.properties().stream()
                .map(pdto -> CategoryProperty.builder()
                        .name(pdto.name())
                        .type(pdto.type())
                        .category(category)
                        .build())
                .collect(Collectors.toSet());

        category.setName(dto.name());
        category.setDescription(dto.description());
        category.setParentCategory(parentCategory);
        category.setProperties(properties);


        log.info("Saving new category to db: {}", category);
        return converter.apply(repository.save(category));
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
