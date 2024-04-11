package ua.com.agroswit.productservice.service.iml;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.com.agroswit.productservice.dto.CategoryDTO;
import ua.com.agroswit.productservice.dto.CategoryDTO.PropertyDTO;
import ua.com.agroswit.productservice.dto.mapper.CategoryMapper;
import ua.com.agroswit.productservice.exceptions.ResourceInConflictStateException;
import ua.com.agroswit.productservice.exceptions.ResourceNotFoundException;
import ua.com.agroswit.productservice.model.Category;
import ua.com.agroswit.productservice.model.CategoryProperty;
import ua.com.agroswit.productservice.repository.CategoryRepository;
import ua.com.agroswit.productservice.service.CategoryService;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository repo;
    private final CategoryMapper mapper;

    @Override
    @Transactional(readOnly = true)
    public List<CategoryDTO> getAllHighLevelCategories() {
        return repo.findAllByParentCategoryId(null).stream()
                .map(mapper::toDTO)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public CategoryDTO getById(Integer id) {
        return repo.findById(id)
                .map(mapper::toDTO)
                .orElseThrow(() -> new ResourceNotFoundException(String.format(
                        "Category with id %d not found", id))
                );
    }

    @Override
    @Transactional
    public CategoryDTO create(CategoryDTO dto) {
        if (repo.existsByName(dto.name())) {
            throw new ResourceInConflictStateException(String.format(
                    "Category with name %s already exists", dto.name())
            );
        }

        Category parentCategory = null;
        if (dto.parentCategoryId() != null) {
            parentCategory = repo.findById(dto.parentCategoryId())
                    .orElseThrow(() -> new ResourceNotFoundException(String.format(
                            "Parent category with id %d not found", dto.parentCategoryId()))
                    );
        }

        var category = mapper.toEntity(dto);
        category.setParentCategory(parentCategory);
        category.getProperties().forEach(p -> p.setCategory(category));

        log.info("Saving new category to db: {}", category);
        return mapper.toDTO(repo.save(category));
    }

    @Override
    @Transactional
    public CategoryDTO updateById(Integer id, CategoryDTO dto) {
        if (id.equals(dto.parentCategoryId())) {
            throw new IllegalArgumentException(
                    "Category cannot be subcategory of itself"
            );
        }

        var category = repo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(String.format(
                        "Category with id %d not found", id))
                );

        Category parentCategory = null;
        if (dto.parentCategoryId() != null) {
            parentCategory = repo.findById(dto.parentCategoryId())
                    .orElseThrow(() -> new ResourceNotFoundException(String.format(
                            "Parent category with id %d not found", dto.parentCategoryId()))
                    );
        }

        mapper.update(dto, category);
        category.setParentCategory(parentCategory);
        var categoryProps = category.getProperties();
        for (var prop : categoryProps) {
            for (var pdto : dto.properties()) {
                if (prop.getName().equals(pdto.name())) {
                    prop.setType(pdto.type());
                    prop.setCategory(category);
                } else {
                    categoryProps.remove(prop);
                    categoryProps.add(new CategoryProperty(null, pdto.name(), pdto.type(), category));
                }
            }
        }

        log.info("Updating category in db: {}", category);
        return mapper.toDTO(repo.save(category));
    }

    @Override
    public void deleteById(Integer id) {
        log.info("Deleting category with id {} from db", id);
        repo.deleteById(id);
    }
}
