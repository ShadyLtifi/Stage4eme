package org.tunilink.tunilink.Repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import org.tunilink.tunilink.Entity.Competence;
@Repository
public interface CompetenceRepository extends MongoRepository<Competence,String> {
    Competence findOneById(String id);
}
