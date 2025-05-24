package id.ac.ui.cs.advprog.buildingstore.authentication.repository;
import id.ac.ui.cs.advprog.buildingstore.authentication.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
}