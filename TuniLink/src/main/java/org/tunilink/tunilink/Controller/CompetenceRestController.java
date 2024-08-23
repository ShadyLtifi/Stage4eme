package org.tunilink.tunilink.Controller;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.tunilink.tunilink.Entity.Competence;
import org.tunilink.tunilink.Service.ICompetenceService;

import java.util.List;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@AllArgsConstructor
public class CompetenceRestController {

    @Autowired
    ICompetenceService competenceService;


    // http://localhost:8585/retrieve-all-Competence
    @GetMapping("/retrieve-all-Competence")
    @ResponseBody
    public List<Competence> getCompetence() {
        List<Competence> listCompetence = competenceService.retrieveAllCompetence();
        return listCompetence ;
    }



    // http://localhost:8585/add-Competence
    @PostMapping("/add-Competence")
    @ResponseBody
    public Competence addCompetence(@RequestBody Competence competence) {
        Competence c= competenceService.createCompetence(competence);
        return c;
    }



    // http://localhost:8585/modify-Competence/{id}
    @PutMapping("/modify-Competence/{id}")
    @ResponseBody
    public Competence modifyCompetence(@RequestBody Competence competence) {
        return competenceService.updateCompetence(competence);
    }



    //    http://localhost:8585/delete-Competence/{id}
    @DeleteMapping("/delete-Competence/{id}")
    @ResponseBody
    public void deleteCompetence(@PathVariable String id) {
        competenceService.deleteCompetence(id);
    }


    // http://localhost:8585/retrieve-Competence/{id}
    @GetMapping("/retrieve-Competence/{id}")
    @ResponseBody
    public Competence retrieveCompetence(@PathVariable("id") String id) {
        return   competenceService.findOnebyId(id);
    }

}
