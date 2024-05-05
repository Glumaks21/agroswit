package ua.com.agroswit.userdetails.service;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ua.com.agroswit.userdetails.dto.request.EntrepreneurModifyingDTO;
import ua.com.agroswit.userdetails.dto.response.EntrepreneurDTO;

public interface EntrepreneurService {
    Page<EntrepreneurDTO> getAll(Pageable pageable);
    EntrepreneurDTO getById(Integer id);
    EntrepreneurDTO create(EntrepreneurModifyingDTO dto);
    EntrepreneurDTO update(Integer id, EntrepreneurModifyingDTO dto);
    void deleteById(Integer id);
}
