package org.tunilink.tunilink.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Data
@Document
@FieldDefaults(level= AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
public class Offre {
    @Id
    String id;
    String title;
    String description;
    private Set<String> requirements;
    String country;
    Long nbrPlace;
    TypeOfrre typeOffre;

    @DBRef
    @EqualsAndHashCode.Exclude
    User user;

    @DBRef
    @JsonIgnore
    private Set<Candidature> candidatures = new HashSet<>();

    @Override
    public String toString() {
        return "Offre [id=" + id + ", title=" + title + ", description=" + description +
                ", requirements=" + requirements + ", country=" + country +
                ", nbrPlace=" + nbrPlace + ", typeOffre=" + typeOffre + "]";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Offre offre = (Offre) o;
        return Objects.equals(id, offre.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
