package school.site.api.repository;

import org.springframework.data.repository.CrudRepository;
import school.site.api.model.User;

public interface UserRepository extends CrudRepository<User, Integer> {

}