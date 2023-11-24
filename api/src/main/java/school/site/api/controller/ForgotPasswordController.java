package school.site.api.controller;

import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import school.site.api.model.User;
import school.site.api.payload.response.MessageResponse;
import school.site.api.repository.UserRepository;
import school.site.api.security.jwt.JwtUtils;
import school.site.api.service.email.EmailSenderService;

import java.io.UnsupportedEncodingException;

@Controller
@RequestMapping("/api/auth")
public class ForgotPasswordController {

    @Autowired
    private EmailSenderService emailSenderService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    JwtUtils jwtUtils;

    @PostMapping("/reset")
    public ResponseEntity resetPassword(@RequestBody String password, HttpServletRequest httpServletRequest){
        String token = jwtUtils.getJwtFromAuthorizationHeader(httpServletRequest);
        Boolean isValid = jwtUtils.validateJwtToken(token);

        if(isValid){
            String email = jwtUtils.getUsernameFromJwtToken(token);
            User existingUser = userRepository.findByEmail(email).orElseThrow();

            if(existingUser != null){
                existingUser.setPassword(encoder.encode(password));
                userRepository.save(existingUser);
                return ResponseEntity.ok().body(new MessageResponse("Password was updated successfully"));
            }
        }
        return ResponseEntity.badRequest().body(new MessageResponse("Internal server error"));
    }


    @PostMapping("/forgot")
    public ResponseEntity forgotPassword(@RequestParam("email") String email){

        if(email != null){
            if(userRepository.existsByEmail(email)){
                String token = jwtUtils.generateTokenFromUsername(email);

                User user = userRepository.findByEmail(email).orElse(null);

                if(user != null){
                    String resetLink = "http://localhost:5173/reset-password?token=" + token;
                    System.out.println(user.getEmail());

                    try {
                        SimpleMailMessage mailMessage = new SimpleMailMessage();
                        mailMessage.setFrom("jelemduduzisa@gmail.com");
                        mailMessage.setTo(user.getEmail());
                        mailMessage.setSubject("Please Complete Registration !!!");
                        mailMessage.setText("To confirm your account registration, please click the link below :"+"\n"+ resetLink);
                        emailSenderService.sendEmail(mailMessage);
                    } catch (Exception e){
                        System.out.println("Failed to send email. Error: " + e.getMessage());
                    }

                    return ResponseEntity.ok().body(new MessageResponse("Email was sent"));
                }
            }
        }

        return ResponseEntity.badRequest().body(new MessageResponse("Internal server error"));
    }
}
