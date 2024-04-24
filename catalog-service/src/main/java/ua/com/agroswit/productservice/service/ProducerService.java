package ua.com.agroswit.productservice.service;

import org.springframework.web.multipart.MultipartFile;
import ua.com.agroswit.productservice.dto.ProducerDTO;

import java.util.List;

public interface ProducerService {
    List<ProducerDTO> getAll();
    ProducerDTO getById(Integer id);
    ProducerDTO getByName(String name);
    ProducerDTO create(ProducerDTO dto);
    String saveLogoById(Integer producerId, MultipartFile logo);
    ProducerDTO update(Integer id, ProducerDTO dto);
    void deleteById(Integer id);
    void removeLogoById(Integer id);
}
