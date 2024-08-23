package org.tunilink.tunilink.Controller;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.tunilink.tunilink.Entity.Candidature;
import org.tunilink.tunilink.Entity.Offre;
import org.tunilink.tunilink.Service.CandidatureService;

import java.util.List;
import java.util.Set;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@AllArgsConstructor
public class CandidatureRestController {

    @Autowired
    CandidatureService candidatureService;


    // http://localhost:8585/retrieve-all-Candidature
    @GetMapping("/retrieve-all-Candidature")
    @ResponseBody
    public List<Candidature> getCandidature() {
        List<Candidature> listCandidature = candidatureService.retrieveAllCandidature();
        return listCandidature ;
    }


    //    http://localhost:8585/delete-Candidature/{id}
    @DeleteMapping("/delete-Candidature/{id}")
    @ResponseBody
    public void deleteCandidature(@PathVariable String id) {
        candidatureService.deleteCandidature(id);
    }


    // http://localhost:8585/retrieve-Candidature/{id}
    @GetMapping("/retrieve-Candidature/{id}")
    @ResponseBody
    public Candidature retrieveCandidature(@PathVariable("id") String id) {
        return candidatureService.findOnebyId(id);
    }


    // http://localhost:8585/accept/{candidatureId}
    @PostMapping("/accept/{candidatureId}")
    public ResponseEntity<String> acceptCandidature(@PathVariable("candidatureId") String candidatureId) {
        try {
            Candidature candidature = candidatureService.findOnebyId(candidatureId);
            if (candidature == null) {
                return ResponseEntity.notFound().build();
            }
            candidatureService.acceptCandidature(candidature);
            return ResponseEntity.ok("Candidature acceptée et nombre de places mis à jour.");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Une erreur s'est produite: " + e.getMessage());
        }
    }

    // http://localhost:8585/retrieve-CandidatureUsername
    @GetMapping("/retrieve-CandidatureUsername")
    @ResponseBody
    public Set<Candidature> retrieveCandidatureByUsername(@RequestParam String username)  {
        return candidatureService.getCandidatureByUsername(username);
    }
}
