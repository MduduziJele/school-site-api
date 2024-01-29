package school.site.api.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
import java.util.Optional;
import java.util.Set;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import school.site.api.service.email.EmailSenderService;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    private static final Logger LOGGER = LogManager.getLogger(UserService.class);

    @Autowired
    EmailSenderService emailSenderService;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    PasswordEncoder encoder;

    public ResponseEntity<MessageResponse> addUser(MultipartFile file, String first_name, String last_name, String email, String mobile_number, Set<String> roles, String password) throws IOException {
        String fileName = sanitizeFileName(file.getOriginalFilename());
        String uploadDirectory = System.getProperty("user.dir") + File.separator + "api/src/main/resources/static/users/profiles/";
        Path imagePath = Paths.get(uploadDirectory, fileName);

        if(userRepository.existsByEmail(email)){
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Email is already in use"));
        }

        try {
            if(!file.isEmpty()){
                Files.write(imagePath, file.getBytes());
            }

            String filePath = fileName.isEmpty() ? "/default.png" : ("/" + fileName);

            User user = new User(first_name, last_name,email, encoder.encode(password), mobile_number, filePath);
            Set<Role> userRoles = assignRoles(roles);
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
                LOGGER.error("Failed to send registration email to {}: {}", email, e.getMessage());
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new MessageResponse("Failed to send registration email"));
            }
            userRepository.save(user);
            return ResponseEntity.ok(new MessageResponse("A user has been successfully added"));
        } catch (IOException e){
            LOGGER.error("Error occurred while processing user registration: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new MessageResponse("Failed to process user registration"));
        } catch (Exception e) {
            LOGGER.error("Failed to send registration email to {}: {}");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new MessageResponse("Failed to send registration email"));
        }
    }
    private Set<Role> assignRoles(Set<String> strRoles){
        Set<Role> userRoles = new HashSet<>();

        if (strRoles == null) {
            Role userRole = roleRepository.findByName(ERole.ROLE_CONTENT_CREATOR)
                    .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
            userRoles.add(userRole);
        } else {
            strRoles.forEach(role -> {
                switch (role) {
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

        return userRoles;
    }
    public User findByEmail(String email) {
       return userRepository.findByEmail(email).get();
    }
    public byte[] getUserImage(Integer id) throws IOException, RuntimeException {
        String uploadDirectory = System.getProperty("user.dir") + File.separator + "api/src/main/resources/static/users/profiles";

        Optional<User> userOptional = userRepository.findById(id);
        if (userOptional.isEmpty()) {
            // Handle case where user is not found, perhaps return a default image or throw an exception
            throw new RuntimeException("User not found for id: " + id);
        }

        User user = userOptional.get();
        String imageName = user.getImage_url();
        byte[] image = new byte[0];

        try {
            File file = new File(uploadDirectory, imageName);
            if (file.exists()) {
                image = Files.readAllBytes(file.toPath());
            } else {
                byte[] defaultImage = new byte[0];
                String getDefaultImage = System.getProperty("user.dir") + File.separator + "api/src/main/resources/static/users/profiles/default.png";
                File defaultFile = new File(getDefaultImage);
                defaultImage = Files.readAllBytes(defaultFile.toPath());
                return defaultImage;
            }
        } catch (IOException e) {
            LOGGER.error("Error reading user image: {}", e.getMessage());
            throw e;
        }
        return image;
    }
    private String sanitizeFileName(String fileName){
        return fileName.replaceAll("[^a-zA-Z0-9-_\\.]", "_");
    }
}
