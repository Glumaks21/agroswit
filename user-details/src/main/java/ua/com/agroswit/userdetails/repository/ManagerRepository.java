package ua.com.agroswit.userdetails.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ua.com.agroswit.userdetails.model.Manager;
import ua.com.agroswit.userdetails.model.enums.UkrainianDistrict;

import java.util.List;

@Repository
public interface ManagerRepository extends JpaRepository<Manager, Integer> {
    List<Manager> findAllByDistrict(UkrainianDistrict district);

    @Query(value = "SELECT order_id FROM manager_orders WHERE manager_id = :id", nativeQuery = true)
    List<Integer> findAllOrderIdsById(Integer id);
}
