package umlerr.serviceusers.repository;

import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import umlerr.serviceusers.model.Users;

@Repository
public interface UsersRepository extends JpaRepository<Users, UUID> {
    Optional<Users> findByEmail(String email);
}
