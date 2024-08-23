package org.tunilink.tunilink.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Data
@FieldDefaults(level= AccessLevel.PRIVATE)
@Setter
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Document
public class User {
    @Id
    private String id;
    private String firstname;
    private String lastname;
    private String cin;
    @Indexed(unique=true)
    @Email
    private String email;
    private LocalDate dateN;
    private String country;
    private String username;
    @Field("role")
    private TypeRole role;
    @NotNull(message = "Password cannot be null")
    @Size(min = 8, message = "Password must be at least 8 characters long")
    private String password;
    private Date updatedAt;
    private Date createdAt;
    private boolean enabled = false ;
    private LocalDate lastLogin ;
    private String verificationToken;

    @DBRef
    @JsonIgnore
    private Set<Candidature> candidatures =new HashSet<>();

    @DBRef
    @JsonIgnore
    @EqualsAndHashCode.Exclude
    private Set<Offre> of =new HashSet<>();

    @DBRef
    @JsonIgnore
    private Set<Experience> experiences =new HashSet<>();

    public boolean getEnabled(){
        return this.enabled;
    }



}
