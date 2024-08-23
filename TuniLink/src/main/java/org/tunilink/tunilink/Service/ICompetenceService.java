package org.tunilink.tunilink.Service;

import org.tunilink.tunilink.Entity.Competence;

import java.util.List;

public interface ICompetenceService {
    Competence createCompetence (Competence c);
    Competence updateCompetence (Competence c);
    Competence findOnebyId (String Id);
    List<Competence> retrieveAllCompetence();
    void deleteCompetence(String Id);
}
