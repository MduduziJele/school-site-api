package school.site.api.service;

import jakarta.servlet.http.HttpServletRequest;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import school.site.api.model.User;
import school.site.api.payload.response.MessageResponse;
import school.site.api.repository.UserRepository;
import school.site.api.security.jwt.JwtUtils;
import school.site.api.service.email.EmailSenderService;

import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


@Service
public class ForgotPasswordService {

    private static final Logger LOGGER = LogManager.getLogger(UserService.class);

    @Autowired
    private EmailSenderService emailSenderService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder encoder;

    @Autowired
    private JwtUtils jwtUtils;

    public MessageResponse resetPassword(String password, HttpServletRequest httpServletRequest){
       try {
           String token = jwtUtils.getJwtFromAuthorizationHeader(httpServletRequest);
           if(!jwtUtils.validateJwtToken(token)) new MessageResponse("Invalid or expired token");
           String email = jwtUtils.getUsernameFromJwtToken(token);
           Optional<User> optionalUser = userRepository.findByEmail(email);
           if(optionalUser.isEmpty()) new MessageResponse("User not found");
           User existingUser = optionalUser.get();
           existingUser.setPassword(encoder.encode(password));
           userRepository.save(existingUser);
           return new MessageResponse("Password updated successfully");
       } catch (Exception e) {
           LOGGER.error("Error resetting password: " + e.getMessage(), e);
           return new MessageResponse("An error occurred while resetting the password");
       }
    }

    public MessageResponse forgotPassword(String email){
        try {
            if(!isValidEmail(email)) new MessageResponse("Invalid email format");
            if(!userRepository.existsByEmail(email)) new MessageResponse("User with the provided email does not exist");
            String token = jwtUtils.generateTokenFromUsername(email);
            User user = userRepository.findByEmail(email).orElse(null);
            if(user == null) new MessageResponse("User not found");

            String resetLink = "http://localhost:5173/reset-password?token=" + token;

            SimpleMailMessage mailMessage = new SimpleMailMessage();
            mailMessage.setFrom("your_email");
            mailMessage.setTo(user.getEmail());
            mailMessage.setSubject("Reset Your Password");
            mailMessage.setText("To reset your password, please click the link below:\n" + resetLink);
            emailSenderService.sendEmail(mailMessage);

            return new MessageResponse("Password reset instructions sent to your email");

        } catch (Exception e) {
            LOGGER.error("Error sending password reset email: " + e.getMessage(), e);
            return new MessageResponse("An error occurred while processing your request");
        }
    }

    private boolean isValidEmail(String email){

        String EMAIL_REGEX = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        Pattern pattern = Pattern.compile(EMAIL_REGEX);

        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }
}
