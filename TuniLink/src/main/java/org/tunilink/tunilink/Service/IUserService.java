package org.tunilink.tunilink.Service;


import org.springframework.web.multipart.MultipartFile;
import org.tunilink.tunilink.Entity.TypeRole;
import org.tunilink.tunilink.Entity.User;


import java.util.List;

public interface IUserService {
    User findByEmail(String email);
    User updateUser (User user);
    void changePassword(String email, String newPassword, String oldPssword);
    User enableOrDisable(String id);
    void saveVerificationToken(String id,String verfi);
    User findByVerificationToken(String verificationToken);
    void disableInactiveAccounts();
    User findbyUsername(String username);

    List<User> retrieveAllUser();


    void deleteUser(String Id);

    TypeRole getRoleByUsername(String username);
    User AffecterUseraOfrre(String username , String idoffre);

    User UploadCv (MultipartFile file, String username);


}
