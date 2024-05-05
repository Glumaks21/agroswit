package ua.com.agroswit.userdetails.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ua.com.agroswit.userdetails.model.Person;

@Repository
public interface PersonRepository extends JpaRepository<Person, Integer> {
    boolean existsByIdNumber(String idNumber);
}
