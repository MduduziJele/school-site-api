package school.site.api.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import school.site.api.model.About;

public interface AboutRepository extends JpaRepository<About, Integer> {

    About findByAboutId(Integer aboutId);
}
