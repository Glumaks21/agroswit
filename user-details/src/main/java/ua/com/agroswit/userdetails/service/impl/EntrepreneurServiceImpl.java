package ua.com.agroswit.userdetails.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.com.agroswit.userdetails.dto.request.EntrepreneurModifyingDTO;
import ua.com.agroswit.userdetails.dto.response.EntrepreneurDTO;
import ua.com.agroswit.userdetails.dto.mapper.EntrepreneurMapper;
import ua.com.agroswit.userdetails.dto.response.UserServiceDTO;
import ua.com.agroswit.userdetails.exception.ResourceInConflictStateException;
import ua.com.agroswit.userdetails.exception.ResourceNotFoundException;
import ua.com.agroswit.userdetails.model.enums.Role;
import ua.com.agroswit.userdetails.repository.CustomerRepository;
import ua.com.agroswit.userdetails.repository.EntrepreneurRepository;
import ua.com.agroswit.userdetails.repository.ManagerRepository;
import ua.com.agroswit.userdetails.repository.PersonRepository;
import ua.com.agroswit.userdetails.service.CustomerService;
import ua.com.agroswit.userdetails.service.EntrepreneurService;

import static ua.com.agroswit.userdetails.model.enums.CustomerType.ENTREPRENEUR;
import static ua.com.agroswit.userdetails.model.enums.Role.USER;

@Slf4j
@Service
@RequiredArgsConstructor
public class EntrepreneurServiceImpl implements EntrepreneurService {

    private final CustomerService customerService;
    private final ManagerRepository managerRepo;
    private final PersonRepository personRepo;
    private final CustomerRepository customerRepo;
    private final EntrepreneurRepository entrepreneurRepo;
    private final EntrepreneurMapper mapper;


    @Override
    @Transactional(readOnly = true)
    public Page<EntrepreneurDTO> getAll(Pageable pageable) {
        var ePage = entrepreneurRepo.findAll(pageable);
        var pairs = customerService.fetchUsers(ePage.getContent());
        var dtos = pairs.stream().map(p -> mapper.toDTO(p.getLeft(), p.getRight())).toList();
        return new PageImpl<>(dtos, pageable, ePage.getTotalElements());
    }

    @Override
    @Transactional(readOnly = true)
    public EntrepreneurDTO getById(Integer id) {
        var entrepreneur = entrepreneurRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Entrepreneur with id %d not found".formatted(id)));
        var user = customerService.fetchUser(entrepreneur);
        return mapper.toDTO(entrepreneur, user);
    }

    @Override
    @Transactional
    public EntrepreneurDTO create(EntrepreneurModifyingDTO dto) {
        checkIfUserHasDetails(dto.customer().userId());
        checkIfIdNumberExists(dto.idNumber());
        checkIfCompanyNameExists(dto.companyName());

        var entrepreneur = mapper.toEntity(dto);
        entrepreneur.setType(ENTREPRENEUR);
        var user = customerService.fetchUser(entrepreneur);
        checkUserRole(user);

        log.info("Saving entrepreneur details to db: {}", entrepreneur);
        entrepreneurRepo.save(entrepreneur);
        return mapper.toDTO(entrepreneur, user);
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

    private void checkIfCompanyNameExists(String companyName) {
        if (entrepreneurRepo.existsByCompanyName(companyName)) {
            throw new ResourceInConflictStateException(
                    "Entrepreneur with company companyName %s already exists".formatted(companyName));
        }
    }

    private void checkUserRole(UserServiceDTO user) {
        if (!user.role().equals(USER)) {
            throw new IllegalArgumentException("User with id %d has not a role %s".formatted(user.id(), USER));
        }
    }

    @Override
    @Transactional
    public EntrepreneurDTO update(Integer id, EntrepreneurModifyingDTO dto) {
        var entrepreneur = entrepreneurRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Entrepreneur with id %d not found".formatted(id)));

        if (!entrepreneur.getIdNumber().equals(dto.idNumber())) {
            checkIfIdNumberExists(dto.idNumber());
        }
        if (!entrepreneur.getCompanyName().equals(dto.companyName())) {
            checkIfCompanyNameExists(dto.companyName());
        }

        var user = customerService.fetchUser(entrepreneur);
        checkUserRole(user);

        log.info("Updating entrepreneur details to db: {}", entrepreneur);
        entrepreneurRepo.save(entrepreneur);
        return mapper.toDTO(entrepreneur, user);
    }

    @Override
    @Transactional
    public void deleteById(Integer id) {
        log.info("Deleting entrepreneur details with id {}", id);
        entrepreneurRepo.deleteById(id);
    }
}
