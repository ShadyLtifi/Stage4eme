package org.tunilink.tunilink.Repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import org.tunilink.tunilink.Entity.Candidature;
import org.tunilink.tunilink.Entity.Offre;
import org.tunilink.tunilink.Entity.User;

import java.util.Set;

@Repository
public interface CandidatureRepository extends MongoRepository<Candidature,String> {
    Candidature findOneById(String id);
    Set<Candidature> findByUser(User user);


}
