package umlerr.servicelistings.repository;

import java.util.Optional;
import java.util.UUID;
import org.jspecify.annotations.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import umlerr.servicelistings.model.Listing;

@Repository
public interface ListingsRepository extends JpaRepository<Listing, UUID> {

    @NonNull
    Page<Listing> findAll(@NonNull Pageable pageable);

    Optional<Listing> findByCarId(UUID carId);
}
