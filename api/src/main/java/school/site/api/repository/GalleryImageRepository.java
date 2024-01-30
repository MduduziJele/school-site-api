package school.site.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import school.site.api.model.GalleryImage;

@Repository
public interface GalleryImageRepository extends JpaRepository<GalleryImage, Long> {

}
