package umlerr.servicecars.repository;

import java.util.List;
import java.util.UUID;
import org.jspecify.annotations.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import umlerr.servicecars.model.Car;

@Repository
public interface CarsRepository extends JpaRepository<Car, UUID> {

    @NonNull
    Page<Car> findAllByActualTrue(@NonNull Pageable pageable);

    @NonNull
    Page<Car> findAllByBrandAndActualTrue(String brand, @NonNull Pageable pageable);

    Car findByVin(String vin);

    @Query("SELECT c.vin FROM Car c")
    List<String> findAllVinNumbers();

    long countByActualTrue();
}
