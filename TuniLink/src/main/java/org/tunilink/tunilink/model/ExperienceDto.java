package org.tunilink.tunilink.model;

import lombok.Getter;
import lombok.Setter;
import org.tunilink.tunilink.Entity.Competence;
import org.tunilink.tunilink.Entity.Experience;

@Setter
@Getter
public class ExperienceDto {
    private Experience experience;
    private Competence competence;
}
