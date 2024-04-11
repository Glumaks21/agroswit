package ua.com.agroswit.productservice.dto.mapper;

import com.google.common.collect.Sets;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ua.com.agroswit.productservice.dto.CategoryDTO;
import ua.com.agroswit.productservice.dto.SimplifiedCategoryDTO;
import ua.com.agroswit.productservice.model.Category;
import ua.com.agroswit.productservice.model.CategoryProperty;
import ua.com.agroswit.productservice.model.enums.PropertyTypeE;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static ua.com.agroswit.productservice.model.enums.PropertyTypeE.NUMBER;
import static ua.com.agroswit.productservice.model.enums.PropertyTypeE.TEXT;


class CategoryMapperTest {

    private static CategoryMapper mapper;

    @BeforeAll
    static void init() {
        mapper = new CategoryMapperImpl();
    }

    @Test
    void should_map_to_DTO() {
        var entity = Category.builder()
                .id(2)
                .name("Sunflowers")
                .description("FLower that likes sun a lot")
                .parentCategory(Category.builder()
                        .id(1)
                        .build()
                )
                .subcategories(Set.of(
                        Category.builder()
                                .id(3)
                                .name("Test1")
                                .build(),
                        Category.builder()
                                .id(4)
                                .name("Test2")
                                .build())
                )
                .build();
        entity.setProperties(Set.of(
                new CategoryProperty(1, "oilness", NUMBER, entity),
                new CategoryProperty(2, "area", TEXT, entity))
        );

        var expectedDto = new CategoryDTO(
                2,
                "Sunflowers",
                "FLower that likes sun a lot",
                1,
                Set.of(
                        new CategoryDTO.PropertyDTO("oilness", NUMBER),
                        new CategoryDTO.PropertyDTO("area", TEXT)
                ),
                Set.of(new SimplifiedCategoryDTO(3, "Test1", null),
                        new SimplifiedCategoryDTO(4, "Test2", null))
        );

        var actual = mapper.toDTO(entity);

        assertEquals(expectedDto.id(), actual.id());
        assertEquals(expectedDto.name(), actual.name());
        assertEquals(expectedDto.description(), actual.description());
        assertEquals(expectedDto.parentCategoryId(), actual.parentCategoryId());
        assertEquals(expectedDto.subcategories(), actual.subcategories());
        assertEquals(expectedDto.properties(), actual.properties());
    }

    @Test
    void should_map_to_entity() {
        var dto = new CategoryDTO(
                2,
                "Sunflowers",
                "FLower that likes sun a lot",
                1,
                Set.of(
                        new CategoryDTO.PropertyDTO("oilness", NUMBER),
                        new CategoryDTO.PropertyDTO("area", TEXT)
                ),
                Set.of(new SimplifiedCategoryDTO(3, "Test1", null),
                        new SimplifiedCategoryDTO(4, "Test2", null))
        );

        var expectedEntity = Category.builder()
                .id(2)
                .name("Sunflowers")
                .description("FLower that likes sun a lot")
                .build();
        expectedEntity.setProperties(Set.of(
                new CategoryProperty(null, "oilness", NUMBER, null),
                new CategoryProperty(null, "area", TEXT, null))
        );

        var actual = mapper.toEntity(dto);

        assertEquals(expectedEntity.getId(), actual.getId());
        assertEquals(expectedEntity.getName(), actual.getName());
        assertEquals(expectedEntity.getDescription(), actual.getDescription());
        assertNull(expectedEntity.getParentCategory());
        assertTrue(actual.getSubcategories().isEmpty());
        assertTrue(actual.getProperties().size() == 2);
    }

    @Test
    void should_update_an_entity() {
        var dto = new CategoryDTO(
                3,
                "Sunflowers",
                null,
                2,
                Sets.newHashSet(
                        new CategoryDTO.PropertyDTO("oilness", NUMBER),
                        new CategoryDTO.PropertyDTO("area", TEXT)
                ),
                Sets.newHashSet(
                        new SimplifiedCategoryDTO(3, "Test1", null),
                        new SimplifiedCategoryDTO(4, "Test2", null)
                )
        );

        var entity = Category.builder()
                .id(2)
                .name("BAN")
                .description("PERMAK")
                .parentCategory(Category.builder()
                        .id(1)
                        .build()
                )
                .properties(Sets.newHashSet(
                        new CategoryProperty(1, "test", NUMBER, null)
                ))
                .build();

        mapper.update(dto, entity);

        assertNotEquals(dto.id(), entity.getId());
        assertEquals(dto.name(), entity.getName());
        assertNull(entity.getDescription());
        assertEquals(1, entity.getProperties().size());
        assertTrue(entity.getSubcategories().isEmpty());
    }
}