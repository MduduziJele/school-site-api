package school.site.api.controller;


import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import school.site.api.payload.response.MessageResponse;
import school.site.api.service.ForgotPasswordService;

@Controller
@RequestMapping("/api/auth")
public class ForgotPasswordController {

    @Autowired
    ForgotPasswordService forgotPasswordService;

    @PostMapping("/reset")
    public ResponseEntity resetPassword(@RequestParam("password") String password, HttpServletRequest httpServletRequest){
        MessageResponse messageResponse = forgotPasswordService.resetPassword(password, httpServletRequest);
        return ResponseEntity.badRequest().body(new MessageResponse("Internal server error"));
    }


    @PostMapping("/forgot")
    public ResponseEntity forgotPassword(@RequestParam("email") String email){
        MessageResponse messageResponse = forgotPasswordService.forgotPassword(email);
        return ResponseEntity.badRequest().body(messageResponse);
    }
}
