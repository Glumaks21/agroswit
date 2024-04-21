package ua.com.agroswit.productservice.service;

import org.springframework.web.multipart.MultipartFile;
import ua.com.agroswit.productservice.dto.ProducerDTO;
import ua.com.agroswit.productservice.dto.request.ProducerModifiableDTO;

import java.util.List;

public interface ProducerService {
    List<ProducerDTO> getAll();
    ProducerDTO getById(Integer id);
    ProducerDTO getByName(String name);
    ProducerDTO create(ProducerModifiableDTO dto);
    ProducerDTO saveLogoById(Integer producerId, MultipartFile logo);
    ProducerDTO update(Integer id, ProducerModifiableDTO dto);
    void deleteById(Integer id);
    void removeLogoById(Integer id);
}
