package school.site.api.controller;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import school.site.api.payload.request.LoginRequest;
import school.site.api.payload.response.MessageResponse;
import school.site.api.payload.response.UserInfoResponse;
import school.site.api.repository.RoleRepository;
import school.site.api.repository.UserRepository;
import school.site.api.security.jwt.JwtUtils;
import school.site.api.security.service.UserDetailsImpl;

import java.util.*;
import java.util.stream.Collectors;


@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    JwtUtils jwtUtils;

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest){
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getEmail(),loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        ResponseCookie jwtCookie = jwtUtils.generateJwtCookie(userDetails);

        List<String> roles = userDetails.getAuthorities().stream().map(item -> item.getAuthority()).collect(Collectors.toList());

        return ResponseEntity.ok().header(HttpHeaders.AUTHORIZATION,"Bearer " + jwtCookie.toString()).body(new UserInfoResponse(userDetails.getId(), userDetails.getFirst_name(),userDetails.getLast_name() ,userDetails.getEmail(), roles, jwtCookie));
    }

   @PostMapping("/signup")
   public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signupRequest){
       System.out.println(signupRequest.getMobile_number());
       if(userRepository.existsByEmail(signupRequest.getEmail())){
           return ResponseEntity.badRequest().body(new MessageResponse("Error: Email is already in use"));
       }

       // Create new user's account
       User user = new User(signupRequest.getFirst_name(), signupRequest.getLast_name(),signupRequest.getEmail(), encoder.encode(signupRequest.getPassword()), signupRequest.getMobile_number(), "/sunset.jpg");

       Set<String> strRoles = signupRequest.getRole();
       Set<Role> roles = new HashSet<>();

       if(strRoles == null){
           Role userRole = roleRepository.findByName(ERole.ROLE_CONTENT_CREATOR)
                   .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
           roles.add(userRole);
       } else {
           strRoles.forEach(role -> {
               switch (role){
                   case "ADMIN":
                       Role adminRole = roleRepository.findByName(ERole.ROLE_ADMIN)
                               .orElseThrow(() -> new RuntimeException("Error: Role is not found"));
                       roles.add(adminRole);

                       break;
                   case "CONTENT_CREATOR":
                       Role modRole = roleRepository.findByName(ERole.ROLE_CONTENT_CREATOR)
                               .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                       roles.add(modRole);

                       break;
                   default:
                       Role userRole = roleRepository.findByName(ERole.ROLE_CONTENT_CREATOR)
                               .orElseThrow(() -> new RuntimeException("Error: Role is not found"));
                       roles.add(userRole);
               }
           });
       }

       user.setRoles(roles);
       userRepository.save(user);

       return ResponseEntity.ok(new MessageResponse("User registered successfully"));
   }

    @PostMapping("/signout")
    public ResponseEntity<?> logoutUser(HttpServletResponse response) {
        // Revoke the JWT token by setting it to null or empty in the headers
        jwtUtils.revokeJwtToken(response);
        // You may also want to add additional logic, e.g., clear any user sessions or perform cleanup
        return ResponseEntity.ok().body(new MessageResponse("You've been signed out"));
    }
}
