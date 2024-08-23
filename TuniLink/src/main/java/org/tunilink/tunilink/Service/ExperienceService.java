package org.tunilink.tunilink.Service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.tunilink.tunilink.Entity.Competence;
import org.tunilink.tunilink.Entity.Experience;
import org.tunilink.tunilink.Entity.Offre;
import org.tunilink.tunilink.Entity.User;
import org.tunilink.tunilink.Repository.CompetenceRepository;
import org.tunilink.tunilink.Repository.ExperienceRepository;
import org.tunilink.tunilink.Repository.UserRepository;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class ExperienceService implements IExperienceService{

    @Autowired
    ExperienceRepository experienceRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    CompetenceRepository competenceRepository;
    @Override
    public Experience createExperience(Experience ex, User u, Competence c) {
        if (u != null) {
            ex.setUser(u);

            Competence savedCompetence = competenceRepository.save(c);
            ex.getCompetences().add(savedCompetence);

            Experience expr = experienceRepository.save(ex);
            u.getExperiences().add(expr);
            experienceRepository.save(ex);
            return expr;
        }
        return null;
    }
    @Override
    public Experience updateExperience(Experience ex) {
        return experienceRepository.save(ex);
    }

    @Override
    public Experience findOnebyId(String Id) {
        return experienceRepository.findOneById(Id);
    }

    @Override
    public List<Experience> retrieveAllExperience() {
        return experienceRepository.findAll();
    }

    @Override
    public void deleteExperience(String Id) {
        experienceRepository.deleteById(Id);
    }

    @Override
    public Set<Experience> retrieveExperienceByUsername(String username){
        User u = userRepository.findUserByUsername(username);
        if (u == null) {
            return new HashSet<>();
        }
        return experienceRepository.findByUser(u);
    }

}

