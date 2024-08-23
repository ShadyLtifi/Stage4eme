package org.tunilink.tunilink.Repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.tunilink.tunilink.Entity.Offre;

import java.util.List;

@Repository
public interface OffreRepository extends MongoRepository<Offre,String> {
    Offre findOneById (String Id);
    List<Offre> findOffreByCountryAndAndTypeOffre(String country, String typeoffre);
    //List<Offre>  retrieveOffreByEmailUser(String email);
}
