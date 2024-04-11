package ua.com.agroswit.productservice.service.iml;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.client.MockRestServiceServer;
import ua.com.agroswit.productservice.dto.ProducerDTO;
import ua.com.agroswit.productservice.dto.mapper.ProducerMapper;
import ua.com.agroswit.productservice.exceptions.ResourceNotFoundException;
import ua.com.agroswit.productservice.model.Producer;
import ua.com.agroswit.productservice.repository.ProducerRepository;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.http.HttpMethod.DELETE;
import static org.springframework.http.HttpMethod.POST;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.test.web.client.ExpectedCount.once;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withStatus;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

@RestClientTest({ProducerServiceImpl.class, ProducerMapper.class})
class ProducerServiceImplSimpleIntegrationTest {

    @Autowired
    ProducerServiceImpl service;

    @Autowired
    MockRestServiceServer server;

    @MockBean
    ProducerRepository repository;

    @Autowired
    ProducerMapper mapper;


    @Test
    void should_get_all() {
        var prod1 = new Producer(1, "logo 1", "name 1");
        var prod2 = new Producer(2, "logo 2", "name 2");
        var prodDTO1 = new ProducerDTO(1, "logo 1", "name 1");
        var prodDTO2 = new ProducerDTO(2, "logo 2", "name 2");

        when(repository.findAll()).thenReturn(List.of(prod1, prod2));

        var actual = service.getAll();

        verify(repository, times(1)).findAll();

        assertEquals(List.of(prodDTO1, prodDTO2), actual);
    }

    @Test
    void should_get_by_id() {
        var prod = new Producer(1, "name 1", "logo 1");
        var prodDTO = new ProducerDTO(1, "name 1", "logo 1");

        when(repository.findById(1)).thenReturn(Optional.of(prod));

        var actual = service.getById(1);

        verify(repository, times(1)).findById(1);

        assertEquals(prodDTO, actual);
    }

    @Test
    void should_throw_not_found_if_not_exists_when_get_by_id() {
        when(repository.findById(1)).thenReturn(Optional.empty());

        var ex = assertThrows(ResourceNotFoundException.class, () -> service.getById(1));

        verify(repository, times(1)).findById(1);

        assertEquals("Producer with id 1 not found", ex.getMessage());
    }

    @Test
    void should_get_by_name() {
        var prod = new Producer(1, "name 1", "logo 1");
        var prodDTO = new ProducerDTO(1, "name 1", "logo 1");

        when(repository.findByName("name 1")).thenReturn(Optional.of(prod));

        var actual = service.getByName("name 1");

        verify(repository, times(1)).findByName("name 1");

        assertEquals(prodDTO, actual);
    }

    @Test
    void should_throw_not_found_if_not_exists_when_get_by_name() {
        when(repository.findByName("name 1")).thenReturn(Optional.empty());

        var ex = assertThrows(ResourceNotFoundException.class, () -> service.getByName("name 1"));

        verify(repository, times(1)).findByName("name 1");

        assertEquals("Producer with name name 1 not found", ex.getMessage());
    }

    @Test
    void should_create() {
        var createDTO = new ProducerDTO(null, "name 1", null);
        var file = new MockMultipartFile("test.png", new byte[]{});
        var prod = new Producer(1, "name 1", "http://localhost:5050/uploads/test.png");
        var expectedDTO = new ProducerDTO(1, "name 1", "http://localhost:5050/uploads/test.png");

        when(repository.existsByName("name 1")).thenReturn(false);
        server.expect(requestTo("http://storage-service/api/v1/uploads"))
                .andExpect(method(POST))
                .andRespond(withStatus(CREATED)
                        .header("Location", "http://localhost:5050/uploads/test.png")
                );
        when(repository.save(any(Producer.class))).thenReturn(prod);

        var actual = service.create(createDTO, file);

        verify(repository, times(1)).existsByName("name 1");
        verify(repository, times(1)).save(any(Producer.class));
        server.verify();

        assertEquals(expectedDTO, actual);
    }

    @Test
    void should_update() {
        var updateDTO = new ProducerDTO(null, "name 2", null);
        var file = new MockMultipartFile("test.png", new byte[]{});
        var fetchedProd = new Producer(1, "name 1", "http://localhost:5050/uploads/test.png");
        var savedProd = new Producer(1, "name 2", "http://localhost:5050/uploads/test2.png");
        var expectedDTO = new ProducerDTO(1, "name 2", "http://localhost:5050/uploads/test2.png");

        when(repository.findById(1)).thenReturn(Optional.of(fetchedProd));
        server.expect(once(), requestTo(fetchedProd.getLogoUrl()))
                .andExpect(method(DELETE))
                .andRespond(withSuccess());
        server.expect(once(), requestTo("http://storage-service/api/v1/uploads"))
                .andExpect(method(POST))
                .andRespond(withStatus(CREATED)
                        .header("Location", "http://localhost:5050/uploads/test2.png")
                );
        when(repository.save(any(Producer.class))).thenReturn(savedProd);

        var actual = service.update(1, updateDTO, file);

        verify(repository, times(1)).findById(1);
        verify(repository, times(1)).save(any(Producer.class));
        server.verify();

        assertEquals(expectedDTO, actual);
    }

    @Test
    void should_delete() {
        service.delete(1);
        verify(repository, times(1)).deleteById(1);
    }
}