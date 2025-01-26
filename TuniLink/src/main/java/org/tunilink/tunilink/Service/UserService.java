package org.tunilink.tunilink.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.tunilink.tunilink.Entity.*;
import org.tunilink.tunilink.Repository.CandidatureRepository;
import org.tunilink.tunilink.Repository.OffreRepository;
import org.tunilink.tunilink.Repository.UserRepository;
import org.tunilink.tunilink.Security.JwtTokenUtil;
import org.webjars.NotFoundException;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;


@Service
public class UserService implements IUserService {
    @Autowired
    private Environment env;
    @Autowired
    private  UserRepository userRepo;
    @Autowired
    private OffreRepository offreRepository;
    @Autowired
    private CandidatureRepository candidatureRepository;
    private PasswordEncoder passwordEncoder;

    private JwtTokenUtil jwttkn;

    public UserService(UserRepository userRepo) {
        this.userRepo = userRepo;
        this.passwordEncoder = new BCryptPasswordEncoder();
    }


    public User findByEmail(String email) {
        if(this.userRepo.findByEmail(email) != null){

            return this.userRepo.findByEmail(email);
        }else{
            User user = new User();
            user.setId(String.valueOf(0L));
            return user ;
        }
    }

    @Override
    public User updateUser(User user) {
        User u = userRepo.findUserByUsername(user.getUsername());
        if (u != null) {
            u.setFirstname(user.getFirstname());
            u.setLastname(user.getLastname());
            u.setDateN(user.getDateN());
            u.setCountry(user.getCountry());
            u.setCin(user.getCin());
            u.setEmail(user.getEmail());
            return userRepo.save(u);
        } else {
            return null;
        }
    }




    @Override
    public void saveVerificationToken(String id, String verfi) {
        User u = userRepo.findUserById( id);
        u.setVerificationToken(verfi);
        userRepo.save(u);
    }

    @Override
    public User findByVerificationToken(String verificationToken) {
        return userRepo.findByVerificationToken(verificationToken);
    }





    @Override
    public User enableOrDisable(String email) {
        User u = userRepo.findByEmail(email);
        if (u != null) {
            boolean isEnabled = u.getEnabled();
            u.setEnabled(!isEnabled);
            userRepo.save(u);
            return u;
        } else {
            throw new NotFoundException("User not found with email: " + email);
        }
    }

    @Override
    public void changePassword(String email, String oldPassword, String newPassword) {
        if(userRepo.existsByEmail(email)) {
            User user = userRepo.findByEmail(email);
            if (passwordEncoder.matches(oldPassword, user.getPassword())) {
                user.setPassword(passwordEncoder.encode(newPassword));
                userRepo.save(user);
            } else {
                throw new BadCredentialsException("Incorrect old password");
            }
        }
    }

    @Scheduled(cron = "0 0 0 * * ?") // Run every day at midnight
    @Override
    public void disableInactiveAccounts() {
        List<User> inactiveUsers = userRepo.findByLastLoginBefore(LocalDate.now().minusDays(90));
        for (User user : inactiveUsers) {
            user.setEnabled(false);
            userRepo.save(user);
        }
    }

    @Override
    public User findbyUsername(String username) {
        return userRepo.findUserByUsername(username);
    }

    @Override
    public List<User> retrieveAllUser() {
        return userRepo.findAll();
    }


    @Override
    public void deleteUser(String username){
        User u = userRepo.findUserByUsername(username);
        userRepo.delete(u);
    }


    @Override
    public TypeRole getRoleByUsername(String username) {
        User user = userRepo.findUserByUsername(username);
        if (user != null) {
            return user.getRole();
        } else {
            throw new RuntimeException("User not found with username: " + username);
        }
    }

    @Override
    public User AffecterUseraOfrre(String username, String idoffre) {
        User u = userRepo.findUserByUsername(username);
        if (u != null) {
            Offre off = offreRepository.findOneById(idoffre);
            if (off != null) {
                // Create a new candidature
                Candidature candidature = new Candidature();
                candidature.setUser(u); // Link the candidature to the user
                candidature.setOffre(off); // Link the candidature to the offer
                candidature.setStatus(Status.PENDING);

                // Save the candidature
                candidatureRepository.save(candidature);

                // Add candidature to user's set
                u.getCandidatures().add(candidature);
                userRepo.save(u);

                // Add candidature to offer's set
                off.getCandidatures().add(candidature);
                offreRepository.save(off);
            } else {
                throw new RuntimeException("Offre not found");
            }
        } else {
            throw new RuntimeException("User not found");
        }
        return u;
    }

    @Override
    public  User UploadCv (MultipartFile file, String username) {
        try {
            String cv = StringUtils.cleanPath(file.getOriginalFilename());
            Path fileStorageLocation = Paths.get(env.getProperty("file.upload-dir"))
                    .toAbsolutePath().normalize();
            Path targetLocation = fileStorageLocation.resolve(cv);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

            // Créer une nouvelle leçon avec le contenu du fichier et le titre, puis sauvegarder dans la base de données
            User u = userRepo.findUserByUsername(username);
            if (u == null) {
                return null;
            }
            u.setCv(cv);
            return userRepo.save(u);
        } catch (Exception ex) {
            System.out.println("Exception: " + ex.getMessage());
            ex.printStackTrace();
            return null;
        }
    }


    public User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
            String username = ((UserDetails) authentication.getPrincipal()).getUsername();
            return userRepo.findUserByUsername(username); // Adjust to your repository method
        }
        return null;
    }

}
