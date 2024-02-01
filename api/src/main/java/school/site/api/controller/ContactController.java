package school.site.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import school.site.api.model.Contact;
import school.site.api.service.ContactService;

import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin("http://localhost:5173")
@RequestMapping("/api/auth/")
public class ContactController {

    @Autowired
    private ContactService contactService;

    @RequestMapping(value = "/contacts")
    public List<Contact> getAllContact(){
        return contactService.getAllContact();
    }

    @RequestMapping(value = "/contact/{id}")
    public Optional<Contact> getContact(@PathVariable Integer id){
        return contactService.getContact(id);
    }

    @RequestMapping(value = "/addContact", method = RequestMethod.POST)
    public void addContact(@RequestBody Contact contact){
        contactService.addContact(contact);
    }

    @RequestMapping(value = "/updateContact/{id}", method = RequestMethod.PUT)
    public void updateContact(@RequestBody Contact contact, @PathVariable Integer id){
        contactService.updateContact(id, contact);
    }


}

