package school.site.api.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import school.site.api.model.ERole;
import school.site.api.model.Role;
import school.site.api.model.User;
import school.site.api.payload.response.MessageResponse;
import school.site.api.repository.RoleRepository;
import school.site.api.repository.UserRepository;

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
    RoleRepository roleRepository;

    @Autowired
    PasswordEncoder encoder;

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/adduser")
    public ResponseEntity<?> addUser(@Valid @RequestPart("image") MultipartFile file, @RequestParam("first_name") String first_name,
                                     @RequestParam("last_name") String last_name,
                                     @RequestParam("email") String email,
                                     @RequestParam("mobile_number") String mobile_number,
                                     @RequestParam("roles") Set<String> roles,
                                     @RequestParam("password") String password
                                     ) throws IOException {
        System.out.println(first_name + last_name + email + mobile_number + roles + password);
        if(userRepository.existsByEmail(email)){
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Email is already in use"));
        }

        if(!file.isEmpty()){
            String uploadDirectory = System.getProperty("user.dir") + File.separator + "api/src/main/resources/static/users/profiles/";
            Path imagePath = Paths.get(uploadDirectory, file.getOriginalFilename());
            Files.write(imagePath, file.getBytes());
        }

        Set<String> strRoles = roles;
        Set<Role> userRoles = new HashSet<>();

        if(strRoles == null){
            Role userRole = roleRepository.findByName(ERole.ROLE_CONTENT_CREATOR)
                    .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
            userRoles.add(userRole);
        } else {
            strRoles.forEach(role -> {
                switch (role){
                    case "ADMIN":
                        Role adminRole = roleRepository.findByName(ERole.ROLE_ADMIN)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found"));
                        userRoles.add(adminRole);

                        break;
                    case "CONTENT_CREATOR":
                        Role modRole = roleRepository.findByName(ERole.ROLE_CONTENT_CREATOR)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        userRoles.add(modRole);

                        break;
                    default:
                        Role userRole = roleRepository.findByName(ERole.ROLE_CONTENT_CREATOR)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found"));
                        userRoles.add(userRole);
                }
            });
        }

        String filePath;

        if(file.getOriginalFilename() != "" || file.getOriginalFilename() != null) {
            filePath ="/" + file.getOriginalFilename();
        } else {
            filePath = "/";
        }

        User user = new User(first_name, last_name,email, encoder.encode(password), mobile_number, filePath);
        user.setRoles(userRoles);
        // Create new user's account
        userRepository.save(user);

        return ResponseEntity.ok(new MessageResponse("User registered successfully"));
    }
}
