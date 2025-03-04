package org.tunilink.tunilink.Service;

import org.tunilink.tunilink.Entity.Candidature;
import org.tunilink.tunilink.Entity.Offre;

import java.util.List;
import java.util.Set;

public interface ICandidatureService {
    Candidature findOnebyId (String Id);
    List<Candidature> retrieveAllCandidature();
    void deleteCandidature(String Id);
    Set<Candidature> getCandidatureByUsername(String username);
    Set<Candidature> getCandidatureByOffer(String idoffer);
}
