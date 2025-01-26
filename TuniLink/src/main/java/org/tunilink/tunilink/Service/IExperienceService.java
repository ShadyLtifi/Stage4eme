package org.tunilink.tunilink.Service;

import org.tunilink.tunilink.Entity.Experience;
import org.tunilink.tunilink.Entity.User;

import java.util.List;
import java.util.Set;

public interface IExperienceService {
    Experience createExperience (Experience ex);
    Experience updateExperience (Experience ex);
    Experience findOnebyId (String Id);
    List<Experience> retrieveAllExperience();
    void deleteExperience(String Id);
    Set<Experience> retrieveExperienceByUsername(String username);

}
