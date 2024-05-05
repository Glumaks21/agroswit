package ua.com.agroswit.userdetails.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ua.com.agroswit.userdetails.dto.request.PersonModifyingDTO;
import ua.com.agroswit.userdetails.dto.response.PersonDTO;

public interface PersonService {
    Page<PersonDTO> getAll(Pageable pageable);
    PersonDTO getById(Integer id);
    PersonDTO create(PersonModifyingDTO dto);
    PersonDTO update(Integer id, PersonModifyingDTO dto);
    void deleteById(Integer id);
}
