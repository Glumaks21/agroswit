package ua.com.agroswit.service;

import org.springframework.web.multipart.MultipartFile;
import ua.com.agroswit.dto.ProducerDTO;
import ua.com.agroswit.model.Producer;

import java.util.List;
import java.util.Optional;

public interface ProducerService {
    List<ProducerDTO> getAll();
    ProducerDTO getById(Integer id);
    ProducerDTO getByName(String name);
    ProducerDTO create(ProducerDTO dto, MultipartFile logo);
    void delete(Integer id);
}
