package ua.com.agroswit.productservice.service.iml;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.cloud.netflix.eureka.EurekaClientAutoConfiguration;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import ua.com.agroswit.productservice.dto.CategoryDTO;
import ua.com.agroswit.productservice.dto.CategoryDTO.PropertyDTO;
import ua.com.agroswit.productservice.dto.SimplifiedCategoryDTO;
import ua.com.agroswit.productservice.exceptions.ResourceInConflictStateException;
import ua.com.agroswit.productservice.exceptions.ResourceNotFoundException;
import ua.com.agroswit.productservice.model.Category;
import ua.com.agroswit.productservice.model.CategoryProperty;
import ua.com.agroswit.productservice.model.enums.PropertyTypeE;
import ua.com.agroswit.productservice.repository.CategoryRepository;
import ua.com.agroswit.productservice.service.CategoryService;

import java.util.Collections;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static ua.com.agroswit.productservice.model.enums.PropertyTypeE.NUMBER;
import static ua.com.agroswit.productservice.model.enums.PropertyTypeE.TEXT;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@EnableAutoConfiguration(exclude = {EurekaClientAutoConfiguration.class})
@ActiveProfiles("test")
@Testcontainers
class CategoryServiceImplIntegrationTest {

    @ServiceConnection
    @Container
    static MySQLContainer<?> mySQLContainer = new MySQLContainer<>("mysql:8.3")
            .withInitScript("init.sql");

    @Autowired
    private CategoryRepository repository;

    @Autowired
    private CategoryService service;

    @BeforeAll
    static void start() {
        mySQLContainer.start();
    }

    @AfterAll
    static void stop() {
        mySQLContainer.stop();
    }

    @BeforeEach
    void clearDb() {
        repository.deleteAll();
    }

    @Test
    void should_get_all_high_level_categories() {
        var seedsCat = Category.builder()
                .name("Seeds")
                .description("Description 1")
                .build();
        var cornsCat = Category.builder()
                .name("Corns")
                .description("Description 2")
                .parentCategory(seedsCat)
                .build();
        var sunflowersCat = Category.builder()
                .name("Sunflowers")
                .description("Description 3")
                .parentCategory(seedsCat)
                .build();

        repository.save(seedsCat);
        repository.saveAll(List.of(cornsCat, sunflowersCat));

        var cornDTO = new SimplifiedCategoryDTO(cornsCat.getId(), "Corns", "Description 2");
        var sunflowerDTO = new SimplifiedCategoryDTO(sunflowersCat.getId(), "Sunflowers", "Description 3");
        var seedsDTO = new CategoryDTO(
                seedsCat.getId(),
                "Seeds",
                "Description 1",
                null,
                Collections.emptySet(),
                Set.of(cornDTO, sunflowerDTO)
        );

        var actual = service.getAllHighLevelCategories();

        assertEquals(1, actual.size());
        assertEquals(seedsDTO, actual.getFirst());
    }

    @Test
    void should_get_by_id() {
        var seedsCat = Category.builder()
                .name("Seeds")
                .description("Description 1")
                .build();

        var testProp1 = new CategoryProperty(null, "test1", PropertyTypeE.NUMBER, null);
        var testProp2 = new CategoryProperty(null, "test2", TEXT, null);

        var sunflowersCat = Category.builder()
                .name("Sunflowers")
                .description("Description 3")
                .parentCategory(seedsCat)
                .properties(Set.of(testProp1, testProp2))
                .build();
        testProp1.setCategory(sunflowersCat);
        testProp2.setCategory(sunflowersCat);

        repository.save(seedsCat);
        repository.save(sunflowersCat);

        var testPropDTO1 = new PropertyDTO("test1", PropertyTypeE.NUMBER);
        var testPropDTO2 = new PropertyDTO("test2", TEXT);

        var expected = new CategoryDTO(
                sunflowersCat.getId(),
                "Sunflowers",
                "Description 3",
                seedsCat.getId(),
                Set.of(testPropDTO1, testPropDTO2),
                Collections.emptySet()
        );


        var actual = service.getById(sunflowersCat.getId());

        assertEquals(expected, actual);
    }

    @Test
    void should_throw_not_found_when_get_by_id() {
        assertThrows(ResourceNotFoundException.class, () -> service.getById(1));
    }

    @Test
    void should_create_high_level_category() {
        var testPropDTO1 = new PropertyDTO("test1", PropertyTypeE.NUMBER);
        var testPropDTO2 = new PropertyDTO("test2", TEXT);

        var fakeSubCat = new SimplifiedCategoryDTO(12, "BAN", "BAN");
        var sunflowersCatDTO = new CategoryDTO(
                10,
                "Sunflowers",
                "Description 3",
                null,
                Set.of(testPropDTO1, testPropDTO2),
                Set.of(fakeSubCat)
        );

        var responseDTO = service.create(sunflowersCatDTO);

        var savedCategoryOptional = repository.findById(responseDTO.id());
        assertTrue(savedCategoryOptional.isPresent());
        var savedCategory = savedCategoryOptional.get();
        assertEquals("Sunflowers", savedCategory.getName());
        assertEquals("Description 3", savedCategory.getDescription());
        assertNull(savedCategory.getParentCategory());
        assertEquals(2, savedCategory.getProperties().size());
        assertTrue(savedCategory.getSubcategories().isEmpty());

        var expectedDTO = new CategoryDTO(
                savedCategory.getId(),
                "Sunflowers",
                "Description 3",
                null,
                Set.of(testPropDTO1, testPropDTO2),
                Collections.emptySet()
        );

        assertEquals(expectedDTO, responseDTO);
    }

    @Test
    void should_create_subcategory() {
        var parentCategory = repository.save(Category.builder()
                .name("BAN")
                .build()
        );

        var testPropDTO1 = new PropertyDTO("test1", PropertyTypeE.NUMBER);
        var testPropDTO2 = new PropertyDTO("test2", TEXT);

        var sunflowersCatDTO = new CategoryDTO(
                10,
                "Sunflowers",
                "Description 3",
                parentCategory.getId(),
                Set.of(testPropDTO1, testPropDTO2),
                Collections.emptySet()
        );

        var responseDTO = service.create(sunflowersCatDTO);

        var savedCategoryOptional = repository.findById(responseDTO.id());
        assertTrue(savedCategoryOptional.isPresent());
        var savedCategory = savedCategoryOptional.get();
        assertEquals("Sunflowers", savedCategory.getName());
        assertEquals("Description 3", savedCategory.getDescription());
        assertEquals(parentCategory, savedCategory.getParentCategory());
        assertEquals(2, savedCategory.getProperties().size());

        var expectedDTO = new CategoryDTO(
                savedCategory.getId(),
                "Sunflowers",
                "Description 3",
                parentCategory.getId(),
                Set.of(testPropDTO1, testPropDTO2),
                Collections.emptySet()
        );

        assertEquals(expectedDTO, responseDTO);
    }

    @Test
    void should_throw_not_found_if_parent_category_dont_exist_when_create() {
        var sunflowersCatDTO = new CategoryDTO(
                null,
                "Sunflowers",
                "Description 3",
                3,
                Collections.emptySet(),
                Collections.emptySet()
        );

        var ex = assertThrows(ResourceNotFoundException.class, () -> service.create(sunflowersCatDTO));
        assertEquals("Parent category with id 3 not found", ex.getMessage());
    }

    @Test
    void should_update_by_id() {
        var cornsCat = Category.builder()
                .name("Corns")
                .description("Description 2")
                .build();
        var catProp1 = new CategoryProperty(null, "test1", TEXT, cornsCat);
        cornsCat.setProperties(Set.of(catProp1));

        repository.save(cornsCat);

        var updateDTO = new CategoryDTO(
                2000,
                "Sunflowers",
                "Sunflowers",
                null,
                Set.of(new PropertyDTO("test2", TEXT),
                        new PropertyDTO("test3", NUMBER)
                ),
                Set.of(new SimplifiedCategoryDTO(cornsCat.getId(), "Corns", null))
        );

        var updatedDTO = service.updateById(cornsCat.getId(), updateDTO);
        cornsCat.getProperties().forEach(p -> p.setCategory(cornsCat));

        var updatedCat = repository.findById(cornsCat.getId()).get();
        assertEquals("Sunflowers", updatedCat.getName());
        assertEquals("Sunflowers", updatedCat.getDescription());
        assertNull(updatedCat.getParentCategory());
        assertEquals(2, updatedCat.getProperties().size());
        assertTrue(updatedCat.getSubcategories().isEmpty());

        var expectedDTO = new CategoryDTO(
                cornsCat.getId(),
                "Sunflowers",
                "Sunflowers",
                null,
                Set.of(new PropertyDTO("test2", TEXT),
                        new PropertyDTO("test3", NUMBER)
                ),
                Collections.emptySet()
        );
        assertEquals(expectedDTO, updatedDTO);
    }

    @Test
    void should_throw_not_found_if_category_dont_exist_when_update_by_id() {
        var sunflowersCat = new CategoryDTO(
                2000,
                "Sunflowers",
                "Sunflowers",
                null,
                Collections.emptySet(),
                Collections.emptySet()
        );

        var ex = assertThrows(ResourceNotFoundException.class, () -> service.updateById(2000, sunflowersCat));

        assertEquals("Category with id 2000 not found", ex.getMessage());
    }

    @Test
    void should_throw_not_found_if_parent_category_not_exists_when_update_by_id() {
        var cornsCat = Category.builder()
                .name("Corns")
                .description("Description 2")
                .build();

        repository.save(cornsCat);

        var sunflowersCat = new CategoryDTO(
                null,
                "Sunflowers",
                "Sunflowers",
                10,
                Collections.emptySet(),
                Collections.emptySet()
        );

        var ex = assertThrows(ResourceNotFoundException.class, () -> service.updateById(cornsCat.getId(), sunflowersCat));

        assertEquals("Parent category with id 10 not found", ex.getMessage());
    }


    @Test
    void should_throw_illegal_agrument_if_parent_cat_self_reference_exists_when_update_by_id() {
        var cornsCat = Category.builder()
                .name("Corns")
                .build();

        repository.save(cornsCat);

        var sunflowersCat = new CategoryDTO(
                null,
                "Sunflowers",
                null,
                cornsCat.getId(),
                Collections.emptySet(),
                Collections.emptySet()
        );

        var ex = assertThrows(IllegalArgumentException.class, () -> service.updateById(cornsCat.getId(), sunflowersCat));

        assertEquals("Category cannot be subcategory of itself", ex.getMessage());
    }

    @Test
    void should_delete_by_id() {
        var seedsCat = Category.builder()
                .name("Seeds")
                .description("Description 1")
                .build();
        repository.save(seedsCat);

        service.deleteById(seedsCat.getId());

        assertTrue(repository.findAll().isEmpty());
    }
}