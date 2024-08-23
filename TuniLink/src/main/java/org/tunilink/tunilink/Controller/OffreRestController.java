package org.tunilink.tunilink.Controller;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.tunilink.tunilink.Entity.Offre;
import org.tunilink.tunilink.Entity.User;
import org.tunilink.tunilink.Repository.OffreRepository;
import org.tunilink.tunilink.Service.IOffreService;
import org.tunilink.tunilink.Service.UserService;

import java.util.List;
import java.util.Set;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@AllArgsConstructor
public class OffreRestController {

   @Autowired
    IOffreService offreService;
   @Autowired
    OffreRepository offRep;
   @Autowired
    UserService userService;


    // http://localhost:8585/retrieve-all-offre
    @GetMapping("/retrieve-all-offre")
    @ResponseBody
    public List<Offre> getOffre() {
        List<Offre> listOffre = offreService.retrieveAllOffre();
        return listOffre ;
    }



    // http://localhost:8585/add-offre
    @PostMapping("/add-offre")
    @ResponseBody
    public ResponseEntity<Offre> createOffre(@RequestParam String email, @RequestBody Offre offre) {
        User user = userService.findByEmail(email);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        Offre createdOffre = offreService.createOffre(user, offre);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdOffre);
    }



    // http://localhost:8585/modify-offre/{id}
    @PutMapping("/modify-offre/{id}")
    @ResponseBody
    public Offre modifyOffre(@RequestBody Offre offre) {
        return offreService.updateOffre(offre);
    }



    //    http://localhost:8585/delete-offre/{id}
    @DeleteMapping("/delete-offre/{id}")
    @ResponseBody
    public void deleteOffre(@PathVariable String id) {
        offreService.deleteOffre(id);
    }


    // http://localhost:8585/retrieve-offre/{id}
    @GetMapping("/retrieve-offre/{id}")
    @ResponseBody
    public Offre retrieveOffre(@PathVariable("id") String id) {
     return   offreService.findOnebyId(id);
    }

    // http://localhost:8585/retrieve-offre/{country}/{typeoffre}
    @GetMapping("/retrieve-offre/{country}/{typeoffre}")
    @ResponseBody
    public List<Offre> retrieveOffreByCountryAndTypeoffre(@PathVariable("country") String country,@PathVariable("typeoffre") String typeoffre) {
        return   offreService.findOffreByCountryAndAndTypeOffre(country,typeoffre);
    }


    // http://localhost:8585/retrieve-offreUsername
    @GetMapping("/retrieve-offreUsername")
    @ResponseBody
    public Set<Offre> retrieveOffreByUsername(@RequestParam String username)  {
        return offreService.retrieveOffreByUsername(username);
    }




}

