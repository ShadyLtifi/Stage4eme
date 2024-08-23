package org.tunilink.tunilink.Repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import org.tunilink.tunilink.Entity.User;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface UserRepository extends MongoRepository<User,String>  {

    User findUserById(String id);
    User findUserByUsername(String username);
    User findByEmail(String email);
    boolean existsByEmail(String email);
    boolean existsByUsername(String username);
    User findByVerificationToken(String verificationToken);
    List<User> findByLastLoginBefore(LocalDate date);
}
