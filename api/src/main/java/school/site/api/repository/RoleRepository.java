package school.site.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import school.site.api.model.ERole;
import school.site.api.model.Role;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Integer> {
    Optional<Role> findByName(ERole name);
}
