package org.tunilink.tunilink.model;

import lombok.Data;
import lombok.ToString;
import org.tunilink.tunilink.Entity.TypeRole;

import java.time.LocalDate;


@ToString
@Data

public class RegisterDto {
    private String email;
    private String username;
    private String firstname;
    private String lastname;
    private String cin;
    private String country;  // Country field included
    private String password;
    private LocalDate dateN;
    private TypeRole role;
}


