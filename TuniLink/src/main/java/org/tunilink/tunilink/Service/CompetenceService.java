package org.tunilink.tunilink.Service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.tunilink.tunilink.Entity.Competence;
import org.tunilink.tunilink.Repository.CompetenceRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CompetenceService implements ICompetenceService{

    @Autowired
    CompetenceRepository competenceRepository;
    @Override
    public Competence createCompetence(Competence c) {
        return competenceRepository.save(c);
    }

    @Override
    public Competence updateCompetence(Competence c) {
        return competenceRepository.save(c);
    }

    @Override
    public Competence findOnebyId(String Id) {
        return competenceRepository.findOneById(Id);
    }

    @Override
    public List<Competence> retrieveAllCompetence() {
        return competenceRepository.findAll();
    }

    @Override
    public void deleteCompetence(String Id) {
        competenceRepository.deleteById(Id);
    }
}
