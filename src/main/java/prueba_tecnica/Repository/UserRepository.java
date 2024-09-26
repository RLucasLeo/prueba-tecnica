package prueba_tecnica.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import prueba_tecnica.Entity.User;
import java.util.Optional;
import java.util.UUID;


public interface UserRepository extends JpaRepository<User, UUID>{

    Optional<User> findByEmail(String email);
    
}
