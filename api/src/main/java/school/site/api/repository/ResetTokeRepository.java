package school.site.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import school.site.api.model.ResetToken;

public interface ResetTokeRepository extends JpaRepository<ResetToken, Integer> {
}
