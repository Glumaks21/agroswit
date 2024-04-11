package ua.com.agroswit.productservice.dto.mapper;

import org.junit.jupiter.api.Test;
import ua.com.agroswit.productservice.dto.ProducerDTO;
import ua.com.agroswit.productservice.model.Producer;

import static org.junit.jupiter.api.Assertions.*;

class ProducerMapperTest {

    static ProducerMapper mapper = new ProducerMapperImpl();

    @Test
    void should_map_to_DTO() {
        var entity = new Producer();
        entity.setId(1);
        entity.setName("Producer 1");
        entity.setLogoUrl("Test logo");

        var expected = new ProducerDTO(1, "Producer 1", "Test logo");

        var actual = mapper.toDTO(entity);

        assertEquals(expected, actual);
    }

    @Test
    void should_map_to_entity() {
        var dto = new ProducerDTO(1, "Producer 1", "Test logo");

        var expected = new Producer();
        expected.setId(1);
        expected.setName("Producer 1");
        expected.setLogoUrl("Test logo");

        var actual = mapper.toEntity(dto);

        assertEquals(expected.getId(), actual.getId());
        assertEquals(expected.getName(), actual.getName());
        assertEquals(expected.getLogoUrl(), actual.getLogoUrl());
    }

    @Test
    void should_update() {
        var dto = new ProducerDTO(1, "Producer 2", null);

        var entity = new Producer();
        entity.setId(2);
        entity.setName("Producer 1");
        entity.setLogoUrl("Test logo");

        mapper.update(dto, entity);

        assertEquals(2, entity.getId());
        assertEquals("Producer 2", entity.getName());
        assertNull(entity.getLogoUrl());
    }
}