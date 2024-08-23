package org.tunilink.tunilink.Entity;

import java.util.function.Consumer;

public enum TypeRole {
    ADMIN, CANDIDATE, RECRUITER;


    public static void forEach(Consumer<TypeRole> action) {
        for (TypeRole role : values()) {
            action.accept(role);
        }
    }
}
