package org.tunilink.tunilink.Controller;


import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.tunilink.tunilink.Entity.TypeRole;
import org.tunilink.tunilink.Entity.User;
import org.tunilink.tunilink.Repository.UserRepository;
import org.tunilink.tunilink.Service.AuthService;
import org.tunilink.tunilink.Service.IUserService;
import org.tunilink.tunilink.Service.UserService;
import org.tunilink.tunilink.errors.PasswordDoesNotMatchTheOld;
import org.tunilink.tunilink.errors.UserNotFoundException;
import org.tunilink.tunilink.model.ChangePasswordRequest;
import org.webjars.NotFoundException;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@AllArgsConstructor
public class UserRestController {

    @Autowired
    private Environment env;
    @Autowired
    IUserService iUserService;
    @Autowired
    private final AuthService authService;
    @Autowired
    UserService userService;



    @GetMapping("findUserByUsername/{username}")
    public ResponseEntity<User> getUserByUsername(@PathVariable("username") String username) {
        User user = iUserService.findbyUsername(username);
        if (user != null) {
            return ResponseEntity.ok(user);
        } else {
            return ResponseEntity.notFound().build();
        }
    }


    @PutMapping("/updateUser/{username}")
    public ResponseEntity<User> editUser(@PathVariable("username") String username, @RequestBody User user) {
        // Make sure to set the username in the User object if it's not already set
        user.setUsername(username);
        User updatedUser = iUserService.updateUser(user);
        return ResponseEntity.ok(updatedUser);
    }

    @PutMapping("/change-password")
    public void changePassword(@RequestBody ChangePasswordRequest request) {
        if (request.getEmail() == null ||
                request.getOldPass().isEmpty() ||
                request.getNewPass() == null) {
            System.out.println("Email, old password, and new password are required");
        }

        if (!authService.isOldPasswordCorrect(request.getEmail(), request.getOldPass())) {
            throw new PasswordDoesNotMatchTheOld("The entered old password does not match the current password");
        }
        String subject = "Password Change Notification";
        String body = "Your password has been changed.\n if it wasnt u, \n Click http://localhost:4200/reset-password/" + request.getEmail() + " to change your password.";
        iUserService.changePassword(request.getEmail(), request.getOldPass(), request.getNewPass());
    }

    @PutMapping("/reset-password/{email}")
    public ResponseEntity<String> resetPassword(@PathVariable("email") String email, @RequestParam String password) {
        try {
            authService.resetPassword(email, password);
            return ResponseEntity.ok("Password reset successfully");
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred");
        }
    }


    @PutMapping("toggelUser")
    User enableOrDisable(@RequestParam String email) {
        return iUserService.enableOrDisable(email);
    }


    @GetMapping("SendEmail")
    public void sendEmail(@RequestParam("email") String email) {
        User user = iUserService.findByEmail(email);
        if (user.getId() == null) {
            throw new UserNotFoundException("User not found by the provided email");
        } else {
            String subject = "Rest your password";
            String body = "Click on the lick below to rest your password \n http://localhost:4200/reset-password/" + email;
        }

    }


    @GetMapping("/allusers")
    @ResponseBody
    public List<User> retrievealluser() {
        return iUserService.retrieveAllUser();
    }


    @GetMapping("/getRoleByUsername/{username}")
    @ResponseBody
    TypeRole getRoleUserByUsername(@PathVariable("username") String username) {
        return iUserService.getRoleByUsername(username);
    }

    //    http://localhost:8585/deleteuser/{username}
    @DeleteMapping("/deleteuser/{username}")
    public void deleteUser(@PathVariable("username") String username) {
        iUserService.deleteUser(username);
    }


    //    http://localhost:8585/affectoffre
    @PostMapping("/affectoffre")
    @ResponseBody
    public User affectoffre(@RequestParam String username, @RequestParam String idoffre) {
        return iUserService.AffecterUseraOfrre(username, idoffre);
    }


    @PostMapping(value = "/uploadCv", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> uploadCv(@RequestParam("file") MultipartFile file,
                                      @RequestParam("username") String username) {
        User user = iUserService.UploadCv(file, username);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User not found or upload failed");
        }
        return ResponseEntity.ok(user);

    }


    @GetMapping("/downloadCv/{username}")
    public ResponseEntity<Resource> downloadCv(@PathVariable String username) {
        try {
            // Retrieve the user by username
            User user = iUserService.findbyUsername(username);

            // Check if the user exists and has a CV
            if (user == null || user.getCv() == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }

            // Get the file path where the CV is stored
            Path fileStorageLocation = Paths.get(env.getProperty("file.upload-dir")).toAbsolutePath().normalize();
            Path filePath = fileStorageLocation.resolve(user.getCv()).normalize();

            // Load the file as a Resource
            Resource resource = new UrlResource(filePath.toUri());

            // Check if the file exists
            if (!resource.exists()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }

            // Return the file with headers for download
            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(Files.probeContentType(filePath)))
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                    .body(resource);

        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }



}


