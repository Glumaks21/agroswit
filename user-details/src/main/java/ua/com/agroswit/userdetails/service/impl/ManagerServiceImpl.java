package ua.com.agroswit.userdetails.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.com.agroswit.userdetails.client.OrderClient;
import ua.com.agroswit.userdetails.client.UserClient;
import ua.com.agroswit.userdetails.dto.request.ManagerModifyingDTO;
import ua.com.agroswit.userdetails.dto.response.ManagerDTO;
import ua.com.agroswit.userdetails.dto.OrderServiceDTO;
import ua.com.agroswit.userdetails.dto.mapper.ManagerMapper;
import ua.com.agroswit.userdetails.dto.response.UserServiceDTO;
import ua.com.agroswit.userdetails.exception.ResourceInConflictStateException;
import ua.com.agroswit.userdetails.exception.ResourceNotFoundException;
import ua.com.agroswit.userdetails.model.Manager;
import ua.com.agroswit.userdetails.model.enums.UkrainianDistrict;
import ua.com.agroswit.userdetails.repository.CustomerRepository;
import ua.com.agroswit.userdetails.repository.ManagerRepository;
import ua.com.agroswit.userdetails.service.ManagerService;

import java.util.List;

import static java.util.stream.Collectors.toSet;
import static ua.com.agroswit.userdetails.model.enums.Role.MANAGER;

@Slf4j
@Service
@RequiredArgsConstructor
public class ManagerServiceImpl implements ManagerService {

    private final UserClient userClient;
    private final OrderClient orderClient;
    private final CustomerRepository customerRepo;
    private final ManagerRepository managerRepo;
    private final ManagerMapper mapper;


    @Override
    @Transactional(readOnly = true)
    public List<ManagerDTO> getAll() {
        var managers = managerRepo.findAll();
        return fetchAndMap(managers);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ManagerDTO> getAllByDistrict(UkrainianDistrict district) {
        var managers = managerRepo.findAllByDistrict(district);
        return fetchAndMap(managers);
    }

    private List<ManagerDTO> fetchAndMap(List<Manager> managers) {
        var userIds = managers.stream().map(Manager::getUserId).collect(toSet());
        var users = userClient.getByIds(userIds);
        return managers.stream().map(m -> {
                    var user = users.stream()
                            .filter(u -> m.getUserId().equals(u.id()))
                            .findFirst()
                            .orElseThrow(() -> new ResourceNotFoundException(
                                    "User with id %d not found".formatted(m.getUserId()))
                            );
                    return mapper.toDTO(m, user);
                })
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public ManagerDTO getById(Integer id) {
        var manager = managerRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Manager details with id %d not found".formatted(id)));
        var user = userClient.getById(id);
        return mapper.toDTO(manager, user);
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrderServiceDTO> getAllOrdersById(Integer id) {
        if (!managerRepo.existsById(id)) {
            throw new ResourceNotFoundException("Manager details with id %d not exists".formatted(id));
        }
        var orderIds = managerRepo.findAllOrderIdsById(id);
        return orderClient.getByIds(orderIds);
    }

    @Override
    @Transactional
    public ManagerDTO create(ManagerModifyingDTO dto) {
        checkIfUserHasDetails(dto.userId());

        var user = userClient.getById(dto.userId());
        checkUserRole(user);

        var manager = mapper.toEntity(dto);
        log.info("Saving manager details to db: {}", manager);
        managerRepo.save(manager);
        return mapper.toDTO(manager, user);
    }

    private void checkIfUserHasDetails(Integer userId) {
        if (customerRepo.existsById(userId) || managerRepo.existsById(userId)) {
            throw new ResourceInConflictStateException("User with id %d already has details".formatted(userId));
        }
    }

    private void checkUserRole(UserServiceDTO user) {
        if (!user.role().equals(MANAGER)) {
            throw new IllegalArgumentException("User with id %d has not a role %s".formatted(user.id(), MANAGER));
        }
    }

    @Override
    @Transactional
    public ManagerDTO update(Integer id, ManagerModifyingDTO dto) {
        var manager = managerRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Manager details with id %d not found".formatted(id)));
        mapper.update(dto, manager);

        var user = userClient.getById(id);
        checkUserRole(user);

        log.info("Updating manager details in db: {}", manager);
        managerRepo.save(manager);
        return mapper.toDTO(manager, user);
    }

    @Override
    @Transactional
    public void deleteById(Integer id) {
        log.info("Deleting manager details with id: {}", id);
        managerRepo.deleteById(id);
    }
}
