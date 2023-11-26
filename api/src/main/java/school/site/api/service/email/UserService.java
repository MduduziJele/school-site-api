package school.site.api.service.email;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
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

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    EmailSenderService emailSenderService;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    PasswordEncoder encoder;

    public ResponseEntity<MessageResponse> addUser(MultipartFile file, String first_name, String last_name, String email, String mobile_number, Set<String> roles, String password) throws IOException {
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
        try {
            SimpleMailMessage mailMessage = new SimpleMailMessage();
            mailMessage.setFrom("jelemduduzisa@gmail.com");
            mailMessage.setTo(user.getEmail());
            mailMessage.setSubject("Registration !!!");
            mailMessage.setText("You have been registered on the school site system. Your password is :" + password);
            emailSenderService.sendEmail(mailMessage);
        } catch (Exception e){
            System.out.println(e.getMessage());
        }
        userRepository.save(user);
        return ResponseEntity.ok(new MessageResponse("A user has been successfully added"));
    }
}
