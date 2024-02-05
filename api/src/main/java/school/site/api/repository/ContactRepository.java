package school.site.api.repository;

import org.springframework.data.repository.CrudRepository;
import school.site.api.model.Contact;

public interface ContactRepository extends CrudRepository<Contact, Integer> {
}
