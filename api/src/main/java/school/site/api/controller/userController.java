package school.site.api.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import school.site.api.model.ERole;
import school.site.api.model.EmailDetails;
import school.site.api.model.Role;
import school.site.api.model.User;
import school.site.api.payload.response.MessageResponse;
import school.site.api.repository.RoleRepository;
import school.site.api.repository.UserRepository;
import school.site.api.service.email.EmailSenderService;
import school.site.api.service.email.UserService;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
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
        ResponseEntity response = userService.addUser(file, first_name, email, last_name, mobile_number, roles, password);
        return response;
    }
}
