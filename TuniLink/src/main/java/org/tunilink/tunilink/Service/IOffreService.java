package org.tunilink.tunilink.Service;


import org.tunilink.tunilink.Entity.Offre;
import org.tunilink.tunilink.Entity.User;

import java.util.List;
import java.util.Set;
import java.util.concurrent.CountDownLatch;

public interface IOffreService {
    Offre createOffre (User u ,Offre o);
    Offre updateOffre (Offre o);
    Offre findOnebyId (String Id);
    List<Offre> retrieveAllOffre();
    void deleteOffre(String Id);
    List<Offre> findOffreByCountryAndAndTypeOffre(String c, String tpf);
    Set<Offre> retrieveOffreByUsername(String username);


}
