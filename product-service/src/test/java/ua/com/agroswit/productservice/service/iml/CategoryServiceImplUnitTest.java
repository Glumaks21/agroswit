package ua.com.agroswit.productservice.service.iml;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ua.com.agroswit.productservice.dto.CategoryDTO;
import ua.com.agroswit.productservice.dto.mapper.CategoryMapper;
import ua.com.agroswit.productservice.exceptions.ResourceInConflictStateException;
import ua.com.agroswit.productservice.exceptions.ResourceNotFoundException;
import ua.com.agroswit.productservice.model.Category;
import ua.com.agroswit.productservice.repository.CategoryRepository;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CategoryServiceImplUnitTest {

    @InjectMocks
    private CategoryServiceImpl categoryService;

    @Mock
    private CategoryRepository categoryRepo;

    @Mock
    private CategoryMapper mapper;


    @Test
    void should_get_all_high_level_categories() {
        var categoryList = new ArrayList<Category>();
        var cat1 = new Category(1, "sunflowers", null, null, new HashSet<>(), new HashSet<>());
        var cat2 = new Category(2, "corns", null, null, new HashSet<>(), new HashSet<>());
        categoryList.add(cat1);
        categoryList.add(cat2);

        var expected = new ArrayList<CategoryDTO>();
        var expCat1 = new CategoryDTO(1, "sunflowers", null, null, new HashSet<>(), new HashSet<>());
        var expCat2 = new CategoryDTO(2, "corns", null, null, new HashSet<>(), new HashSet<>());
        expected.add(expCat1);
        expected.add(expCat2);

        when(categoryRepo.findAllByParentCategoryId(null)).thenReturn(categoryList);
        when(mapper.toDTO(cat1)).thenReturn(expCat1);
        when(mapper.toDTO(cat2)).thenReturn(expCat2);

        var actual = categoryService.getAllHighLevelCategories();

        verify(categoryRepo, times(1)).findAllByParentCategoryId(null);
        verify(mapper, times(1)).toDTO(cat1);
        verify(mapper, times(1)).toDTO(cat2);

        assertEquals(expected, actual);
    }

    @Test
    void should_get_by_id() {
        var entity = new Category(2, "corns", null, null, new HashSet<>(), new HashSet<>());
        var dto = new CategoryDTO(2, "corns", null, null, new HashSet<>(), new HashSet<>());

        when(categoryRepo.findById(2)).thenReturn(Optional.of(entity));
        when(mapper.toDTO(entity)).thenReturn(dto);

        var actual = categoryService.getById(2);

        verify(categoryRepo, times(1)).findById(2);
        verify(mapper, times(1)).toDTO(entity);

        assertEquals(dto, actual);
    }

    @Test
    void should_throw_not_found_when_get_by_id() {
        when(categoryRepo.findById(2)).thenReturn(Optional.empty());

        var ex = assertThrows(ResourceNotFoundException.class, () -> categoryService.getById(2));

        verify(categoryRepo, times(1)).findById(2);

        assertEquals("Category with id 2 not found", ex.getMessage());
    }

    @Test
    void should_create() {
        var createDto = new CategoryDTO(null, "corns", "description", 1, new HashSet<>(), new HashSet<>());
        var parentCategory = new Category(1, "seeds", null, null, new HashSet<>(), new HashSet<>());
        var entity = new Category(null, "corns", "description", parentCategory, new HashSet<>(), new HashSet<>());
        var savedCategory = new Category(2, "corns", "description", parentCategory, new HashSet<>(), new HashSet<>());
        var savedDto = new CategoryDTO(2, "corns", "description", 1, new HashSet<>(), new HashSet<>());

        when(categoryRepo.existsByName("corns")).thenReturn(false);
        when(categoryRepo.findById(1)).thenReturn(Optional.of(parentCategory));
        when(mapper.toEntity(createDto)).thenReturn(entity);
        when(categoryRepo.save(entity)).thenReturn(savedCategory);
        when(mapper.toDTO(savedCategory)).thenReturn(savedDto);

        var actual = categoryService.create(createDto);

        verify(categoryRepo, times(1)).existsByName("corns");
        verify(categoryRepo, times(1)).findById(1);
        verify(mapper, times(1)).toEntity(createDto);
        verify(categoryRepo, times(1)).save(entity);
        verify(mapper, times(1)).toDTO(savedCategory);

        assertEquals(savedDto, actual);
    }

    @Test
    void should_throw_conflict_if_name_exists_when_create() {
        var createDto = new CategoryDTO(null, "corns", "description", 1, new HashSet<>(), new HashSet<>());

        when(categoryRepo.existsByName("corns")).thenReturn(true);

        var ex = assertThrows(ResourceInConflictStateException.class, () -> categoryService.create(createDto));

        assertEquals("Category with name corns already exists", ex.getMessage());
    }

    @Test
    void should_throw_not_found_if_parent_category_not_exists_when_create() {
        var createDto = new CategoryDTO(null, "corns", "description", 1, new HashSet<>(), new HashSet<>());

        when(categoryRepo.existsByName("corns")).thenReturn(false);
        when(categoryRepo.findById(1)).thenReturn(Optional.empty());

        var ex = assertThrows(ResourceNotFoundException.class, () -> categoryService.create(createDto));

        assertEquals("Parent category with id 1 not found", ex.getMessage());
    }

    @Test
    void should_update_by_id() {
        var updateDto = new CategoryDTO(null, "sunflowers","plants vs zombies gg", 1, new HashSet<>(), new HashSet<>());
        var parentCategory = new Category(1, "seeds", null, null, new HashSet<>(), new HashSet<>());
        var entity = new Category(2, "corns", "description", null, new HashSet<>(), new HashSet<>());
        var updatedCategory = new Category(2, "sunflowers", "plants vs zombies gg", parentCategory, new HashSet<>(), new HashSet<>());
        var updatedDTO = new CategoryDTO(2, "sunflowers", "plants vs zombies gg", 1, new HashSet<>(), new HashSet<>());

        when(categoryRepo.findById(2)).thenReturn(Optional.of(entity));
        when(categoryRepo.findById(1)).thenReturn(Optional.of(parentCategory));
        when(categoryRepo.save(entity)).thenReturn(updatedCategory);
        when(mapper.toDTO(updatedCategory)).thenReturn(updatedDTO);

        var actual = categoryService.updateById(2, updateDto);

        verify(categoryRepo, times(1)).findById(1);
        verify(categoryRepo, times(1)).findById(2);
        verify(mapper, times(1)).update(updateDto, entity);
        verify(categoryRepo, times(1)).save(updatedCategory);
        verify(mapper, times(1)).toDTO(updatedCategory);

        assertEquals(updatedDTO, actual);
    }

    @Test
    void should_delete_by_id() {
        categoryService.deleteById(1);
        verify(categoryRepo, times(1)).deleteById(1);
    }
}