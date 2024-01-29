package school.site.api.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import school.site.api.repository.RoleRepository;
import school.site.api.repository.UserRepository;
import school.site.api.service.email.EmailSenderService;
import school.site.api.service.UserService;

import java.io.IOException;
import java.util.Set;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
public class userController {

    @Autowired
    UserRepository userRepository;

    @Autowired
    EmailSenderService emailSenderService;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    UserService userService;

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/adduser")
    public ResponseEntity<?> addUser(@Valid @RequestPart("image") MultipartFile file, @RequestParam("first_name") String first_name,
                                     @RequestParam("last_name") String last_name,
                                     @RequestParam("email") String email,
                                     @RequestParam("mobile_number") String mobile_number,
                                     @RequestParam("roles") Set<String> roles,
                                     @RequestParam("password") String password
                                     ) throws IOException {
        ResponseEntity response = userService.addUser(file, first_name, last_name, email, mobile_number, roles, password);
        return response;
    }

    @GetMapping("/user/image/{id}")
    public ResponseEntity<byte[]> getUserImage(@PathVariable("id") Integer id) throws java.io.IOException {
        byte[] image = userService.getUserImage(id);
        return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(image);
    }

}
