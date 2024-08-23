package org.tunilink.tunilink.Controller;


import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.tunilink.tunilink.Entity.Competence;
import org.tunilink.tunilink.Entity.Experience;
import org.tunilink.tunilink.Entity.Offre;
import org.tunilink.tunilink.Entity.User;
import org.tunilink.tunilink.Repository.UserRepository;
import org.tunilink.tunilink.Service.IExperienceService;
import org.tunilink.tunilink.Service.UserService;
import org.tunilink.tunilink.model.ExperienceDto;

import java.util.List;
import java.util.Set;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@AllArgsConstructor
public class ExperienceRestController {

    @Autowired
    IExperienceService experienceService;
    @Autowired
    UserService userService;

    // http://localhost:8585/retrieve-all-Experience
    @GetMapping("/retrieve-all-Experience")
    @ResponseBody
    public List<Experience> getExperience() {
        List<Experience> listExperience = experienceService.retrieveAllExperience();
        return listExperience ;
    }





    // http://localhost:8585/add-Experience
    @PostMapping("/add-Experience")
    @ResponseBody
    public ResponseEntity<Experience> addExperience(@RequestParam String email, @RequestBody ExperienceDto experienceDto) {
        User user = userService.findByEmail(email);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        Experience ex = experienceDto.getExperience();
        Competence c = experienceDto.getCompetence();
        Experience e = experienceService.createExperience(ex, user, c);
        return ResponseEntity.status(HttpStatus.CREATED).body(e);
    }



    // http://localhost:8585/modify-Experience/{id}
    @PutMapping("/modify-Experience/{id}")
    @ResponseBody
    public Experience modifyExperience(@RequestBody Experience e) {
        return experienceService.updateExperience(e);
    }



    //    http://localhost:8585/delete-Experience/{id}
    @DeleteMapping("/delete-Experience/{id}")
    @ResponseBody
    public void deleteExperience(@PathVariable String id) {
        experienceService.deleteExperience(id);
    }


    // http://localhost:8585/retrieve-Experience/{id}
    @GetMapping("/retrieve-Experience/{id}")
    @ResponseBody
    public Experience retrieveExperience(@PathVariable("id") String id) {
        return   experienceService.findOnebyId(id);
    }

    // http://localhost:8585/retrieve-ExperienceUsername
    @GetMapping("/retrieve-ExperienceUsername")
    @ResponseBody
    public Set<Experience> retrieveExperienceByUsername(@RequestParam String username)  {
        return experienceService.retrieveExperienceByUsername(username);
    }


}
