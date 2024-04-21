package ua.com.agroswit.productservice.service.iml;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import ua.com.agroswit.productservice.dto.request.CategoryModifiableDTO;
import ua.com.agroswit.productservice.dto.response.CategoryDTO;
import ua.com.agroswit.productservice.dto.response.SimplifiedCategoryDTO;
import ua.com.agroswit.productservice.dto.mapper.CategoryMapper;
import ua.com.agroswit.productservice.dto.response.SimpleDetailedProductDTO;
import ua.com.agroswit.productservice.exceptions.ResourceInConflictStateException;
import ua.com.agroswit.productservice.exceptions.ResourceNotFoundException;
import ua.com.agroswit.productservice.model.Category;
import ua.com.agroswit.productservice.model.Filter;
import ua.com.agroswit.productservice.repository.CategoryRepository;
import ua.com.agroswit.productservice.repository.FilterRepository;
import ua.com.agroswit.productservice.service.CategoryService;
import ua.com.agroswit.productservice.service.ProductService;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepo;
    private final FilterRepository filterRepo;
    private final MinioUploadService uploadService;
    private final ProductService productService;
    private final CategoryMapper mapper;


    @Override
    @Transactional(readOnly = true)
    public List<SimplifiedCategoryDTO> getAll() {
        return categoryRepo.findAll().stream()
                .map(c -> mapper.toSimplifiedDTO(c, uploadService.getUrl(c.getLogo())))
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<SimplifiedCategoryDTO> getAllLowLevel() {
        return categoryRepo.findAllLowLevelCategories().stream()
                .map(c -> mapper.toSimplifiedDTO(c, uploadService.getUrl(c.getLogo())))
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public CategoryDTO getById(Integer id) {
        return categoryRepo.findById(id)
                .map(c -> mapper.toDTO(c, uploadService.getUrl(c.getLogo())))
                .orElseThrow(() -> new ResourceNotFoundException(String.format(
                        "Category with id %d not found", id))
                );
    }

    @Override
    @Transactional(readOnly = true)
    public CategoryDTO getByName(String name) {
        return categoryRepo.findByNameIgnoreCase(name)
                .map(c -> mapper.toDTO(c, uploadService.getUrl(c.getLogo())))
                .orElseThrow(() -> new ResourceNotFoundException(String.format(
                        "Category with name %s not found", name))
                );
    }

    @Override
    @Transactional(readOnly = true)
    public Page<SimpleDetailedProductDTO> getAllProductsById(Integer id, Pageable pageable) {
        if (!categoryRepo.existsById(id)) {
            throw new ResourceNotFoundException(String.format(
                    "Category with id %d not found", id)
            );
        }
        return productService.getAllDetailedByCategoryId(pageable, id);
    }

    @Override
    @Transactional
    public CategoryDTO create(CategoryModifiableDTO dto) {
        if (categoryRepo.existsByNameIgnoreCase(dto.getName())) {
            throw new ResourceInConflictStateException(String.format(
                    "Category with name %s already exists", dto.getName())
            );
        }

        var category = mapper.toEntity(dto);
        fetchRelations(category, dto);

        log.info("Saving new category to db: {}", category);
        var savedCategory = categoryRepo.save(category);
        return mapper.toDTO(savedCategory, uploadService.getUrl(savedCategory.getLogo()));
    }

    private void fetchRelations(Category category, CategoryModifiableDTO dto) {
        fetchParentCategory(category, dto);
        fetchFilters(category, dto);
    }

    private void fetchParentCategory(Category category, CategoryModifiableDTO dto) {
        var parentCategory = category.getParentCategory();
        if (dto.getParentCategoryId() != null) {
            if (parentCategory == null || !parentCategory.getId().equals(dto.getParentCategoryId())) {
                parentCategory = categoryRepo.findById(dto.getParentCategoryId())
                        .orElseThrow(() -> new ResourceNotFoundException(String.format(
                                "Parent category with id %d not found", dto.getParentCategoryId()))
                        );
            }
        }

        category.setParentCategory(parentCategory);
    }

    private void fetchFilters(Category category, CategoryModifiableDTO dto) {
        List<Filter> filters = category.getFilters();
        if (dto.getFilterIds() != null && !dto.getFilterIds().isEmpty()) {
            var count = filterRepo.countByIdIn(dto.getFilterIds());

            if (count != dto.getFilterIds().size()) {
                throw new ResourceNotFoundException(String.format(
                        "Some filters not found in list: %s", dto.getFilterIds())
                );
            }

            filters = dto.getFilterIds().stream()
                    .map(id -> {
                        var filter = new Filter();
                        filter.setId(id);
                        return filter;
                    })
                    .collect(Collectors.toCollection(ArrayList::new));
        }

        category.setFilters(filters);
    }

    @Override
    @Transactional
    public CategoryDTO saveLogo(Integer categoryId, MultipartFile logo) {
        var category = categoryRepo.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException(String.format(
                        "Category with id %d not found", categoryId))
                );

        log.trace("Saving category logo to storage: {}", logo.getOriginalFilename());
        var logoName = uploadService.uploadImage(logo);

        if (category.getLogo() != null) {
            log.trace("Removing category previous logo from storage: {}", logo.getOriginalFilename());
            uploadService.remove(category.getLogo());
        }

        category.setLogo(logoName);
        var savedCategory = categoryRepo.save(category);
        return mapper.toDTO(savedCategory, uploadService.getUrl(savedCategory.getLogo()));
    }

    @Override
    @Transactional
    public CategoryDTO update(Integer id, CategoryModifiableDTO dto) {
        var category = validateUpdate(id, dto);

        fetchRelations(category, dto);
        log.trace("Removing property groups for category with id: {}", id);
        categoryRepo.deleteAllCategoryPropertyGroupsById(id);
        mapper.update(dto, category);
        log.info("Updating category in db: {}", category);
        categoryRepo.save(category);

        return mapper.toDTO(category, uploadService.getUrl(category.getLogo()));
    }

    private Category validateUpdate(Integer id, CategoryModifiableDTO dto) {
        if (id.equals(dto.getParentCategoryId())) {
            throw new IllegalArgumentException("Category cannot be subcategory of itself");
        }

        var category = categoryRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(String.format(
                        "Category with id %d not found", id))
                );

        if (category.getName() != null && !category.getName().equals(dto.getName())
                && categoryRepo.existsByNameIgnoreCase(dto.getName())) {
            throw new ResourceInConflictStateException(String.format(
                    "Category with name %s already exists", dto.getName())
            );
        }

        return category;
    }

    @Override
    @Transactional
    public void deleteById(Integer id, Integer replaceCategoryId) {
        var optionalCat = categoryRepo.findById(id);
        if (optionalCat.isEmpty()) {
            return;
        }

        var category = optionalCat.get();
        if (replaceCategoryId != null) {
            var replaceCategory = categoryRepo.findById(replaceCategoryId)
                    .orElseThrow(() -> new ResourceNotFoundException(String.format(
                            "Replace category with id %d not found", replaceCategoryId))
                    );

            category.getProducts().forEach(p -> p.setCategory(replaceCategory));
        } else {
            category.getProducts().forEach(p -> {
                p.setCategory(null);
                p.setActive(false);
            });
        }

        if (category.getLogo() != null) {
            log.trace("Removing category logo from storage: {}", category.getLogo());
            uploadService.remove(category.getLogo());
        }

        log.info("Deleting category with id {} from db", id);
        categoryRepo.deleteById(id);
    }

    @Override
    @Transactional
    public void deleteLogoById(Integer categoryId) {
        var category = categoryRepo.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException(String.format(
                        "Category with id %d not found", categoryId))
                );

        if (category.getLogo() != null) {
            log.trace("Removing category logo from storage: {}", category.getLogo());
            uploadService.remove(category.getLogo());

            category.setLogo(null);
            log.trace("Updating category without logo in db: {}", category);
            categoryRepo.save(category);
        }
    }
}