package ua.com.agroswit.service.iml;

import jakarta.validation.ValidationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.com.agroswit.dto.request.CategoryCreationDTO;
import ua.com.agroswit.dto.response.CategoryDTO;
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


    @Override
    public List<Category> getAllCategories() {
        return repository.findAllByParentCategoryId(null);
    }

    @Transactional
    @Override
    public List<Category> getAllSubcategories(Integer id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Category with id " + id + " not exists");
        }
        return repository.findAllByParentCategoryId(id);
    }

    @Transactional
    @Override
    public Category createCategory(CategoryCreationDTO dto) {
        dto.properties().forEach(p1 -> dto.properties().stream()
                .filter(p2 -> p1.name().equals(p2.name()))
                .findFirst()
                .ifPresent(identical -> {
                    throw new RequestValidationException("properties", String.format(
                            "There are identical names is properties: %s", identical.name())
                    );
                })
        );

        if (repository.existsByName(dto.name())) {
            throw new ResourceInConflictStateException(String.format(
                    "Category with name %s already exists", dto.name())
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
        category.setProperties(properties);
        category.setName(dto.name());
        category.setDescription(dto.description());

        log.info("Saving new category to db: {}", category);
        return repository.save(category);
    }

    @Transactional
    @Override
    public Category createSubcategory(CategoryCreationDTO dto, Integer parentId) {
        dto.properties().forEach(p1 -> dto.properties().stream()
                .filter(p2 -> p1.name().equals(p2.name()))
                .findFirst()
                .ifPresent(identical -> {
                    throw new ValidationException(String.format(
                            "There are identical names is properties: %s", identical.name())
                    );
                })
        );

        if (repository.existsByName(dto.name())) {
            throw new ResourceInConflictStateException(String.format(
                    "Category with name %s already exists", dto.name())
            );
        }

        var parentCategory = repository.findById(parentId)
                .orElseThrow(() -> new ResourceNotFoundException(String.format(
                        "Category with id %d not found", parentId))
                );

        var subcategory = new Category();
        var properties = dto.properties().stream()
                        .map(pdto -> CategoryProperty.builder()
                                .name(pdto.name())
                                .type(pdto.type())
                                .category(subcategory)
                                .build())
                        .collect(Collectors.toSet());
        subcategory.setName(dto.name());
        subcategory.setDescription(dto.description());
        subcategory.setProperties(properties);
        subcategory.setParentCategory(parentCategory);

        log.info("Saving subcategory to db: {}", subcategory);
        return repository.save(subcategory);
    }
}
