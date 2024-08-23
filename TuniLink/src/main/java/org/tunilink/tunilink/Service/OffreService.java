package org.tunilink.tunilink.Service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.tunilink.tunilink.Entity.Offre;
import org.tunilink.tunilink.Entity.User;
import org.tunilink.tunilink.Repository.OffreRepository;
import org.tunilink.tunilink.Repository.UserRepository;

import java.util.*;

@Service
@RequiredArgsConstructor
public class OffreService implements IOffreService{

    @Autowired
    OffreRepository offreRepository;
    @Autowired
    UserRepository userRepository;
    @Override
    public Offre createOffre(User u, Offre o) {
        if (u != null) {
            o.setUser(u);
            Offre savedOffre = offreRepository.save(o);
            u.getOf().add(savedOffre);
            userRepository.save(u);
            return savedOffre;
        }
        return null;
    }
    @Override
    public Offre updateOffre(Offre o) {
        return offreRepository.save(o);
    }

    @Override
    public Offre findOnebyId(String Id) {
        return offreRepository.findOneById(Id);
    }

    @Override
    public List<Offre> retrieveAllOffre() {
        return offreRepository.findAll();
    }

    @Override
    public void deleteOffre(String Id) {
        offreRepository.deleteById(Id);
    }

    @Override
    public List<Offre> findOffreByCountryAndAndTypeOffre(String c, String tpf) {
        return offreRepository.findOffreByCountryAndAndTypeOffre(c,tpf);
    }

    @Override
    public Set<Offre> retrieveOffreByUsername(String username) {
        Set<Offre> offress = new HashSet<>();
        List<Offre> l = offreRepository.findAll();
        if (l == null){
            return null;
        }else{
        for (Offre offre : l ){
            User u = offre.getUser();
            if (u != null){
                if(Objects.equals(u.getUsername(), username)){
                    offress.add(offre);
            }

        }
        }
        return offress;
        }

    }
}
