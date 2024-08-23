package org.tunilink.tunilink.Controller;


import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.tunilink.tunilink.Entity.Otp;
import org.tunilink.tunilink.Entity.User;
import org.tunilink.tunilink.Repository.UserRepository;
import org.tunilink.tunilink.Security.JwtIssuer;
import org.tunilink.tunilink.Security.JwtTokenUtil;
import org.tunilink.tunilink.Service.AuthService;
import org.tunilink.tunilink.Service.EmailService;
import org.tunilink.tunilink.Service.OtpService;
import org.tunilink.tunilink.errors.InvalidCredentials;
import org.tunilink.tunilink.errors.UserNotFoundException;
import org.tunilink.tunilink.model.LoginRequest;
import org.tunilink.tunilink.model.LoginResponce;
import org.tunilink.tunilink.model.RegisterDto;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequiredArgsConstructor
public class AuthRestController {

    private final UserRepository userRepo;
    private final OtpService otpService;
    public final JwtIssuer jwtIssuer;
    private final AuthenticationManager authenticationManager;
    private final AuthService authService;
    @Autowired
    private EmailService emailService;

    @PostMapping("/auth/login")
    public ResponseEntity<?> login(@RequestBody @Validated LoginRequest loginRequest) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword())
            );
        } catch (AuthenticationException e) {
            throw new InvalidCredentials("Invalid username/password supplied");
        }

        final UserDetails userDetails = authService.loadUserByUsername(loginRequest.getUsername());
        final String token = JwtTokenUtil.generateToken(userDetails);

        return ResponseEntity.ok(new LoginResponce(token));
    }

    @PostMapping("/auth/register")
    public ResponseEntity<String> registerUser(@RequestBody RegisterDto registerDto) {
        User user = userRepo.findByEmail(registerDto.getEmail());
        if (user != null) {
            throw new UserNotFoundException("User exist by that email");
        }
        authService.registerUser(registerDto);
        emailService.sendCodeByMail(registerDto.getEmail());
        return ResponseEntity.ok("User registered successfully");
    }

    @GetMapping("/verify")
    public ResponseEntity<String> verifyUser(@RequestParam("verificationToken") String verificationToken) {
        if (validateVerificationToken(verificationToken)) {
            return ResponseEntity.ok("User verified successfully");
        } else {
            return ResponseEntity.badRequest().body("Invalid verification token");
        }
    }
    private boolean validateVerificationToken(String verificationToken) {
        User u = userRepo.findByVerificationToken(verificationToken);
        if (u.getVerificationToken().equals(verificationToken)) {
            if (u.getEnabled()) {
                return true;
            }else if(!u.getEnabled()){
                u.setEnabled(true);
                userRepo.save(u);
                return true;
            }
        }
        return false;
    }

    @PostMapping("/generate-otp")
    public ResponseEntity<String> generateOtp(@RequestParam("email") String email) {
        User user = userRepo.findByEmail(email);
        if (user != null) {
            String otp = otpService.generateOTP();
            emailService.sendOTPEmail(user.getEmail(), otp);
            otpService.saveOTP(user.getEmail(), otp);
            return ResponseEntity.ok("OTP sent to your email");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }
    }

    @PostMapping("/verify-otp")
    public ResponseEntity<String> verifyOtp(@RequestBody Otp otpRequest) {
        boolean isValid = otpService.verifyOTP(otpRequest.getEmail(), otpRequest.getOtp());
        if (isValid) {
            return ResponseEntity.ok("OTP verified successfully");
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid or expired OTP");
        }
    }
}
