package org.tunilink.tunilink.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.HashSet;
import java.util.Set;

@Data
@Document
@FieldDefaults(level= AccessLevel.PRIVATE)
@Setter
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Experience {
    @Id
    String id;
    String titre;
    String duree;
    String nomEntreprise;
    String poste;
    String descripton;

    @DBRef
    User user;

    @DBRef
    private Set<Competence> competences =new HashSet<>();
}
