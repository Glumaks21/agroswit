package ua.com.agroswit.service.iml;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.com.agroswit.dto.request.SubCategoryCreationDTO;
import ua.com.agroswit.exception.ResourceInConflictStateException;
import ua.com.agroswit.exception.ResourceNotFoundException;
import ua.com.agroswit.model.SubCategory;
import ua.com.agroswit.model.SubCategoryProperty;
import ua.com.agroswit.repository.CategoryRepository;
import ua.com.agroswit.repository.SubCategoryRepository;
import ua.com.agroswit.service.SubCategoryService;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class SubCategoryServiceImpl implements SubCategoryService {

    private final CategoryRepository categoryRepository;
    private final SubCategoryRepository subcategoryRepository;

    @Override
    public List<SubCategory> getAllByCategoryId(Integer categoryId) {
        return subcategoryRepository.findAllByCategoryId(categoryId);
    }

    @Override
    @Transactional
    public SubCategory create(Integer categoryId, SubCategoryCreationDTO dto) {
        if (subcategoryRepository.existsByNameAndCategoryId(dto.name(), categoryId)) {
            throw new ResourceInConflictStateException(String.format(
                    "Subcategory with name \"%s\" already exists in category with id %d",
                    dto.name(), categoryId
            ));
        }

        var category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException(String.format(
                        "Category with id %d not found", categoryId))
                );

        var subcategory = new SubCategory();

        var properties = dto.properties().stream()
                .map(pdto -> SubCategoryProperty.builder()
                        .name(pdto.name())
                        .type(pdto.type())
                        .subcategory(subcategory)
                        .build()
                )
                .collect(Collectors.toSet());

        subcategory.setName(dto.name());
        subcategory.setCategory(category);
        subcategory.setProperties(properties);

        log.info("Saving subcategory to db: {}", subcategory);
        return subcategoryRepository.save(subcategory);
    }
}
