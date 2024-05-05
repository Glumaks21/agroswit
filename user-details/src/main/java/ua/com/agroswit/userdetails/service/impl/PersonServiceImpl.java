package ua.com.agroswit.userdetails.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.com.agroswit.userdetails.client.UserClient;
import ua.com.agroswit.userdetails.dto.mapper.PersonMapper;
import ua.com.agroswit.userdetails.dto.request.PersonModifyingDTO;
import ua.com.agroswit.userdetails.dto.response.PersonDTO;
import ua.com.agroswit.userdetails.dto.response.UserServiceDTO;
import ua.com.agroswit.userdetails.exception.ResourceInConflictStateException;
import ua.com.agroswit.userdetails.exception.ResourceNotFoundException;
import ua.com.agroswit.userdetails.model.Customer;
import ua.com.agroswit.userdetails.model.enums.CustomerType;
import ua.com.agroswit.userdetails.repository.CustomerRepository;
import ua.com.agroswit.userdetails.repository.EntrepreneurRepository;
import ua.com.agroswit.userdetails.repository.ManagerRepository;
import ua.com.agroswit.userdetails.repository.PersonRepository;
import ua.com.agroswit.userdetails.service.CustomerService;
import ua.com.agroswit.userdetails.service.PersonService;

import static java.util.stream.Collectors.toSet;
import static ua.com.agroswit.userdetails.model.enums.CustomerType.PERSON;
import static ua.com.agroswit.userdetails.model.enums.Role.USER;

@Slf4j
@Service
@RequiredArgsConstructor
public class PersonServiceImpl implements PersonService {

    private final CustomerService customerService;
    private final CustomerRepository customerRepo;
    private final ManagerRepository managerRepo;
    private final EntrepreneurRepository entrepreneurRepo;
    private final PersonRepository personRepo;
    private final PersonMapper mapper;


    @Override
    @Transactional(readOnly = true)
    public Page<PersonDTO> getAll(Pageable pageable) {
        var personPage = personRepo.findAll(pageable);
        var pairs = customerService.fetchUsers(personPage.getContent());
        var dtos = pairs.stream().map(p -> mapper.toDTO(p.getLeft(), p.getRight())).toList();
        return new PageImpl<>(dtos, pageable, personPage.getTotalElements());
    }

    @Override
    @Transactional(readOnly = true)
    public PersonDTO getById(Integer id) {
        var person = personRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Person details with id %d not found".formatted(id)));
        var user = customerService.fetchUser(person);
        return mapper.toDTO(person, user);
    }

    @Override
    @Transactional
    public PersonDTO create(PersonModifyingDTO dto) {
        checkIfUserHasDetails(dto.customer().userId());
        checkIfIdNumberExists(dto.idNumber());

        var person = mapper.toEntity(dto);
        person.setType(PERSON);
        var user = customerService.fetchUser(person);
        checkUserRole(user);

        log.info("Saving person to db: {}", person);
        personRepo.save(person);
        return mapper.toDTO(person, user);
    }

    private void checkIfUserHasDetails(Integer userId) {
        if (managerRepo.existsById(userId) || customerRepo.existsById(userId)) {
            throw new ResourceInConflictStateException("User with id %d already has details".formatted(userId));
        }
    }

    private void checkIfIdNumberExists(String idNumber) {
        if (entrepreneurRepo.existsByIdNumber(idNumber) || personRepo.existsByIdNumber(idNumber)) {
            throw new ResourceInConflictStateException("ID number %s already exists".formatted(idNumber));
        }
    }

    private void checkUserRole(UserServiceDTO user) {
        if (!user.role().equals(USER)) {
            throw new IllegalArgumentException("User with id %d has not a role %s".formatted(user.id(), USER));
        }
    }

    @Override
    @Transactional
    public PersonDTO update(Integer id, PersonModifyingDTO dto) {
        var person = personRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Person details with id %d not found".formatted(id)));

        if (!person.getIdNumber().equals(dto.idNumber())) {
            checkIfIdNumberExists(dto.idNumber());
        }

        var user = customerService.fetchUser(person);
        checkUserRole(user);

        mapper.update(dto, person);
        log.info("Updating person in db: {}", person);
        personRepo.save(person);
        return mapper.toDTO(person, user);
    }

    @Override
    @Transactional
    public void deleteById(Integer id) {
        log.info("Deleting person from db with id: {}", id);
        personRepo.deleteById(id);
    }
}
