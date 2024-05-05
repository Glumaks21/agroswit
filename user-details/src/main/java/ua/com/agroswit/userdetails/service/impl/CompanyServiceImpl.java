package ua.com.agroswit.userdetails.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.com.agroswit.userdetails.dto.request.CompanyModifyingDTO;
import ua.com.agroswit.userdetails.dto.response.CompanyDTO;
import ua.com.agroswit.userdetails.dto.mapper.CompanyMapper;
import ua.com.agroswit.userdetails.dto.response.UserServiceDTO;
import ua.com.agroswit.userdetails.exception.ResourceInConflictStateException;
import ua.com.agroswit.userdetails.exception.ResourceNotFoundException;
import ua.com.agroswit.userdetails.repository.CompanyRepository;
import ua.com.agroswit.userdetails.repository.CustomerRepository;
import ua.com.agroswit.userdetails.repository.ManagerRepository;
import ua.com.agroswit.userdetails.service.CompanyService;
import ua.com.agroswit.userdetails.service.CustomerService;


import static ua.com.agroswit.userdetails.model.enums.CustomerType.COMPANY;
import static ua.com.agroswit.userdetails.model.enums.Role.USER;

@Slf4j
@Service
@RequiredArgsConstructor
public class CompanyServiceImpl implements CompanyService {

    private final CustomerService customerService;
    private final CompanyRepository companyRepo;
    private final CustomerRepository customerRepo;
    private final ManagerRepository managerRepo;
    private final CompanyMapper mapper;


    @Override
    @Transactional(readOnly = true)
    public Page<CompanyDTO> getAll(Pageable pageable) {
        var companyPage = companyRepo.findAll(pageable);
        var pairs = customerService.fetchUsers(companyPage.getContent());
        var dtos = pairs.stream().map(p -> mapper.toDTO(p.getLeft(), p.getRight())).toList();
        return new PageImpl<>(dtos, pageable, companyPage.getTotalElements());
    }

    @Override
    @Transactional(readOnly = true)
    public CompanyDTO getById(Integer id) {
        var company = companyRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Company details with id %d not found".formatted(id)));
        var user = customerService.fetchUser(company);
        return mapper.toDTO(company, user);
    }

    @Override
    @Transactional
    public CompanyDTO create(CompanyModifyingDTO dto) {
        checkIfUserHasDetails(dto.customer().userId());
        checkIfEgrpouExists(dto.egrpou());
        checkIfCompanyNameExists(dto.companyName());

        var company = mapper.toEntity(dto);
        company.setType(COMPANY);
        var user = customerService.fetchUser(company);
        checkUserRole(user);

        log.info("Saving company to db: {}", company);
        companyRepo.save(company);
        return mapper.toDTO(company, user);
    }

    private void checkIfUserHasDetails(Integer userId) {
        if (managerRepo.existsById(userId) || customerRepo.existsById(userId)) {
            throw new ResourceInConflictStateException("User with id %d already has details".formatted(userId));
        }
    }

    private void checkIfEgrpouExists(String egrpou) {
        if (companyRepo.existsByEgrpou(egrpou)) {
            throw new ResourceInConflictStateException("Company with EGRPOU %s already exists".formatted(egrpou));
        }
    }

    private void checkIfCompanyNameExists(String companyName) {
        if (companyRepo.existsByCompanyName(companyName)) {
            throw new ResourceInConflictStateException(
                    "Company with company companyName %s already exists".formatted(companyName));
        }
    }

    private void checkUserRole(UserServiceDTO user) {
        if (!user.role().equals(USER)) {
            throw new IllegalArgumentException("User with id %d has not a role %s".formatted(user.id(), USER));
        }
    }

    @Override
    @Transactional
    public CompanyDTO update(Integer id, CompanyModifyingDTO dto) {
        var company = companyRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Company details with id %d not found".formatted(id)));

        if (!company.getEgrpou().equals(dto.egrpou())) {
            checkIfEgrpouExists(dto.egrpou());
        }
        if (!company.getCompanyName().equals(dto.companyName())) {
            checkIfCompanyNameExists(dto.companyName());
        }

        var user = customerService.fetchUser(company);
        checkUserRole(user);

        mapper.update(dto, company);
        log.info("Updating company in db: {}", company);
        companyRepo.save(company);
        return mapper.toDTO(company, user);
    }

    @Override
    @Transactional
    public void deleteById(Integer id) {
        log.info("Deleting company by id: {}", id);
        companyRepo.deleteById(id);
    }
}
