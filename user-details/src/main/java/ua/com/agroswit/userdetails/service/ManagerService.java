package ua.com.agroswit.userdetails.service;

import ua.com.agroswit.userdetails.dto.request.ManagerModifyingDTO;
import ua.com.agroswit.userdetails.dto.response.ManagerDTO;
import ua.com.agroswit.userdetails.dto.OrderServiceDTO;
import ua.com.agroswit.userdetails.model.enums.UkrainianDistrict;

import java.util.List;

public interface ManagerService {
    List<ManagerDTO> getAll();
    List<ManagerDTO> getAllByDistrict(UkrainianDistrict district);
    ManagerDTO getById(Integer id);
    List<OrderServiceDTO> getAllOrdersById(Integer id);
    ManagerDTO create(ManagerModifyingDTO dto);
    ManagerDTO update(Integer id, ManagerModifyingDTO dto);
    void deleteById(Integer id);
}
