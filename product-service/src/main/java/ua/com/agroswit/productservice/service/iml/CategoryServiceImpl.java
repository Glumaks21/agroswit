package ua.com.agroswit.productservice.service.iml;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import ua.com.agroswit.productservice.dto.CategoryDTO;
import ua.com.agroswit.productservice.dto.SimplifiedCategoryDTO;
import ua.com.agroswit.productservice.dto.mapper.CategoryMapper;
import ua.com.agroswit.productservice.dto.mapper.SimplifiedCategoryMapper;
import ua.com.agroswit.productservice.exceptions.ResourceInConflictStateException;
import ua.com.agroswit.productservice.exceptions.ResourceNotFoundException;
import ua.com.agroswit.productservice.model.Category;
import ua.com.agroswit.productservice.repository.CategoryRepository;
import ua.com.agroswit.productservice.service.CategoryService;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepo;
    private final MinioUploadService uploadService;
    private final CategoryMapper categoryMapper;
    private final SimplifiedCategoryMapper simpleCategoryMapper;


    @Override
    @Transactional(readOnly = true)
    public List<SimplifiedCategoryDTO> getAll() {
        return categoryRepo.findAll().stream()
                .map(c -> simpleCategoryMapper.toDTO(c, uploadService.getUrl(c.getLogo())))
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<SimplifiedCategoryDTO> getAllLowLevel() {
        return categoryRepo.findAllLowLevelCategories().stream()
                .map(c -> simpleCategoryMapper.toDTO(c, uploadService.getUrl(c.getLogo())))
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public CategoryDTO getById(Integer id) {
        return categoryRepo.findById(id)
                .map(c -> categoryMapper.toDTO(c, uploadService.getUrl(c.getLogo())))
                .orElseThrow(() -> new ResourceNotFoundException(String.format(
                        "Category with id %d not found", id))
                );
    }

    @Override
    @Transactional(readOnly = true)
    public CategoryDTO getByName(String name) {
        return categoryRepo.findByName(name)
                .map(c -> categoryMapper.toDTO(c, uploadService.getUrl(c.getLogo())))
                .orElseThrow(() -> new ResourceNotFoundException(String.format(
                        "Category with name %s not found", name))
                );
    }

    @Override
    @Transactional
    public CategoryDTO create(CategoryDTO dto) {
        if (categoryRepo.existsByName(dto.name())) {
            throw new ResourceInConflictStateException(String.format(
                    "Category with name %s already exists", dto.name())
            );
        }

        Category parentCategory = null;
        if (dto.parentCategoryId() != null) {
            parentCategory = categoryRepo.findById(dto.parentCategoryId())
                    .orElseThrow(() -> new ResourceNotFoundException(String.format(
                            "Parent category with id %d not found", dto.parentCategoryId()))
                    );
        }

        var category = categoryMapper.toEntity(dto);
        category.setParentCategory(parentCategory);
        category.getProperties().forEach(p -> p.setCategory(category));

        log.info("Saving new category to db: {}", category);
        var savedCategory = categoryRepo.save(category);
        return categoryMapper.toDTO(savedCategory, uploadService.getUrl(savedCategory.getLogo()));
    }

    @Override
    @Transactional
    public CategoryDTO saveLogo(Integer categoryId, MultipartFile logo) {
        var category = categoryRepo.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException(String.format(
                        "Category with id %d not found", categoryId))
                );

        if (category.getLogo() != null) {
            log.trace("Removing category logo from storage: {}", logo.getOriginalFilename());
            uploadService.remove(category.getLogo());
        }

        log.trace("Saving category logo to storage: {}", logo.getOriginalFilename());
        var logoName = uploadService.uploadImage(logo);

        category.setLogo(logoName);
        var savedCategory = categoryRepo.save(category);
        return categoryMapper.toDTO(savedCategory, uploadService.getUrl(savedCategory.getLogo()));
    }

    @Override
    @Transactional
    public CategoryDTO updateById(Integer id, CategoryDTO dto) {
        if (id.equals(dto.parentCategoryId())) {
            throw new IllegalArgumentException(
                    "Category cannot be subcategory of itself"
            );
        }
        if (categoryRepo.existsByName(dto.name())) {
            throw new ResourceInConflictStateException(String.format(
                    "Category with name %s already exists", dto.name())
            );
        }

        var category = categoryRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(String.format(
                        "Category with id %d not found", id))
                );

        Category parentCategory = null;
        if (dto.parentCategoryId() != null) {
            parentCategory = categoryRepo.findById(dto.parentCategoryId())
                    .orElseThrow(() -> new ResourceNotFoundException(String.format(
                            "Parent category with id %d not found", dto.parentCategoryId()))
                    );
        }

        categoryMapper.update(dto, category);
        category.setParentCategory(parentCategory);

        log.info("Updating category in db: {}", category);
        var updatedCategory = categoryRepo.save(category);
        return categoryMapper.toDTO(updatedCategory, uploadService.getUrl(updatedCategory.getLogo()));
    }

    @Override
    @Transactional
    public void deleteById(Integer id, Integer replaceCategoryId) {
        var optionalCat = categoryRepo.findById(id);
        if (optionalCat.isEmpty()) {
            return;
        }

        var category = optionalCat.get();
        if (category.getLogo() != null) {
            log.trace("Removing category logo from storage: {}", category.getLogo());
            uploadService.remove(category.getLogo());
        }

        if (replaceCategoryId != null) {
            var replaceCategory = categoryRepo.findById(replaceCategoryId)
                    .orElseThrow(() -> new ResourceNotFoundException(String.format(
                            "Replace category with id %d not found", replaceCategoryId))
                    );
            replaceProductCategory(category, replaceCategory);
        } else {
            deactivateCategoryProducts(category);
        }

        log.info("Updating category products with id in db: {}", category.getId());
        categoryRepo.save(category);
        log.info("Deleting category with id {} from db", id);
        categoryRepo.deleteById(id);
    }

    private void deactivateCategoryProducts(Category category) {
        category.getProducts().forEach(p -> {
            p.setCategory(null);
            p.setActive(false);
        });
    }

    private void replaceProductCategory(Category category, Category newCategory) {
        category.getProducts().forEach(p -> p.setCategory(newCategory));
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
