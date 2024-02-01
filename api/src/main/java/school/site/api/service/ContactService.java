package school.site.api.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import school.site.api.model.Contact;
import school.site.api.repository.ContactRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ContactService {
    @Autowired
    private ContactRepository contactRepository;

    public List<Contact> getAllContact(){
        List<Contact> contacts = new ArrayList<>();

        contactRepository.findAll().forEach(contacts::add);

        return contacts;
    }

    public void addContact(Contact contact){ contactRepository.save(contact); }

    public Optional<Contact> getContact(Integer id) { return contactRepository.findById(id); }

    //public void updateContact(Integer id, Contact contact){contactRepository.save(contact); }

    public void updateContact(Integer id, Contact newContact){
        contactRepository.findById(id)
                .map(contact -> {
                    contact.setFb_link(newContact.getFb_link());
                    contact.setInstagram_link(newContact.getInstagram_link());
                    contact.setSchool_address(newContact.getSchool_address());
                    contact.setSchool_email(newContact.getSchool_email());
                    contact.setTiktok_link(newContact.getTiktok_link());
                    contact.setSchool_phone(newContact.getSchool_phone());
                    return contactRepository.save(contact);
                });
    }

}

