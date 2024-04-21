package ua.com.agroswit.productservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ua.com.agroswit.productservice.model.Pesticide;
import ua.com.agroswit.productservice.repository.view.PesticideCombinationView;

import java.util.List;

@Repository
public interface PesticideRepository extends JpaRepository<Pesticide, Integer> {
    @Query(value = """
            SELECT
            IF(main_id != :id, main_id, combo_id) as pesticide_id,
            main_proportion,
            combo_proportion as pesticideProportion
            FROM pesticide_combinations
            WHERE main_id = :id OR combo_id = :id
            """, nativeQuery = true)
    List<PesticideCombinationView> findAllCombinationsByPesticideId(Integer id);
}
