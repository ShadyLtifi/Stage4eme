package org.tunilink.tunilink.Repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import org.tunilink.tunilink.Entity.Candidature;
import org.tunilink.tunilink.Entity.Experience;
import org.tunilink.tunilink.Entity.User;

import java.util.Set;

@Repository
public interface ExperienceRepository extends MongoRepository<Experience,String> {
    Experience findOneById(String Id);
    Set<Experience> findByUser(User user);

}
