package org.tunilink.tunilink.Service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.tunilink.tunilink.Entity.Candidature;
import org.tunilink.tunilink.Entity.Offre;
import org.tunilink.tunilink.Entity.Status;
import org.tunilink.tunilink.Entity.User;
import org.tunilink.tunilink.Repository.CandidatureRepository;
import org.tunilink.tunilink.Repository.OffreRepository;
import org.tunilink.tunilink.Repository.UserRepository;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class CandidatureService implements ICandidatureService{

    @Autowired
    CandidatureRepository candidatureRepository;

    @Autowired
    private OffreRepository offreRepository;
    @Autowired
    private UserRepository userRepository;


    @Override
    public Candidature findOnebyId(String Id) {
        return candidatureRepository.findOneById(Id);
    }
    @Override
    public List<Candidature> retrieveAllCandidature() {
        return candidatureRepository.findAll();
    }

    @Override
    public void deleteCandidature(String Id) {
        candidatureRepository.deleteById(Id);
    }

    @Transactional
    public void acceptCandidature(Candidature candidature) {
        candidature.setStatus(Status.ACCEPTED);
        candidatureRepository.save(candidature);
        Offre offre = candidature.getOffre();
        if (offre != null) {
            Long nb = offre.getNbrPlace();
            if (nb != null) {
                nb -= 1;
                offre.setNbrPlace(nb);
                offreRepository.save(offre);
            }
        }
    }

    @Override
    public Set<Candidature> getCandidatureByUsername(String username) {
        User u = userRepository.findUserByUsername(username);
        if (u == null) {
            return new HashSet<>();
        }
        return candidatureRepository.findByUser(u);
    }
}
