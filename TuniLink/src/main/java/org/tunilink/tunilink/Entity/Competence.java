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
@ToString
@Document
@FieldDefaults(level= AccessLevel.PRIVATE)
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Competence {
    @Id
    String id;
    String nomComp;
    Niveau niveau;

    @DBRef
    @JsonIgnore
    private Set<Experience> experiences =new HashSet<>();
}
