package org.tunilink.tunilink.Controller;


import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.tunilink.tunilink.Entity.TypeRole;
import org.tunilink.tunilink.Entity.User;
import org.tunilink.tunilink.Repository.UserRepository;
import org.tunilink.tunilink.Service.AuthService;
import org.tunilink.tunilink.Service.IUserService;
import org.tunilink.tunilink.errors.PasswordDoesNotMatchTheOld;
import org.tunilink.tunilink.errors.UserNotFoundException;
import org.tunilink.tunilink.model.ChangePasswordRequest;
import org.webjars.NotFoundException;

import java.util.List;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@AllArgsConstructor
public class UserRestController {

    @Autowired
    IUserService iUserService;
    @Autowired
    private final AuthService authService;


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

}


