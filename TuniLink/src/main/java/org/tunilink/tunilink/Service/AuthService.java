package org.tunilink.tunilink.Service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.tunilink.tunilink.Entity.User;
import org.tunilink.tunilink.Repository.UserRepository;
import org.tunilink.tunilink.Security.JwtIssuer;
import org.tunilink.tunilink.Security.UserPrincipal;
import org.tunilink.tunilink.model.RegisterDto;
import org.webjars.NotFoundException;
import java.time.LocalDate;
import java.util.*;

@Service
@RequiredArgsConstructor
public class AuthService implements UserDetailsService {

    private final JwtIssuer jwtIssuer;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepo;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepo.findUserByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("User not found with username: " + username);
        }

        SimpleGrantedAuthority authority = new SimpleGrantedAuthority("ROLE_" + user.getRole());

        return new UserPrincipal(
                user.getId(),
                user.getUsername(),
                user.getEnabled(),
                user.getPassword(),
                Collections.singletonList(authority)
        );
    }
    private Set getAuthority(User user) {
        Set<SimpleGrantedAuthority> authorities = new HashSet<>();
        user.getRole().forEach(role -> {
            authorities.add(new SimpleGrantedAuthority("ROLE_" + role));
        });
        return authorities;
    }

    public void registerUser(RegisterDto registerDto) {
        if (userRepo.existsByEmail(registerDto.getEmail()) && userRepo.existsByUsername(registerDto.getUsername())) {
            throw new RuntimeException("Username is taken!");
        }
        User user = new User();
        user.setFirstname(registerDto.getFirstname());
        user.setLastname(registerDto.getLastname());
        user.setCin(registerDto.getCin());
        user.setUsername(registerDto.getUsername());
        user.setDateN(registerDto.getDateN());
        user.setCountry(registerDto.getCountry());
        user.setUpdatedAt(new Date());
        user.setEmail(registerDto.getEmail());
        user.setPassword(passwordEncoder.encode(registerDto.getPassword()));
        user.setRole(registerDto.getRole());
        user.setCreatedAt(new Date());

        String token = generateVerificationToken();
        user.setVerificationToken(token);

        System.out.println(user);
        userRepo.save(user);
    }

    private String generateVerificationToken() {
        return UUID.randomUUID().toString();
    }

    public void resetPassword(String email, String password) {
        User u = userRepo.findByEmail(email);
        System.out.println(email);
        if (u != null) {
            u.setPassword(passwordEncoder.encode(password));
            System.out.println("done");
            u.setUpdatedAt(new Date());
            userRepo.save(u);
        } else {
            throw new NotFoundException("User not found with email: " + email);
        }
    }

    public boolean isOldPasswordCorrect(String email, String oldPass) {
        User user = userRepo.findByEmail(email);
        if (user != null) {
            return passwordEncoder.matches(oldPass, user.getPassword());
        } else {
            return false;
        }
    }

    public void lastLogin(String email) {
        User u = userRepo.findByEmail(email);
        u.setLastLogin(LocalDate.now());
        userRepo.save(u);
    }


}
