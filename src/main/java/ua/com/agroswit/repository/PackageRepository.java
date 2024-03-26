package ua.com.agroswit.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ua.com.agroswit.model.Package;

@Repository
public interface PackageRepository extends JpaRepository<Package, Integer> {
}
