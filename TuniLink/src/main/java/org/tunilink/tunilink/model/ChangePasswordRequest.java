package org.tunilink.tunilink.model;

import lombok.Builder;
import lombok.Data;
import lombok.Setter;

@Data
@Builder
public class ChangePasswordRequest {
    @Setter
    private String oldPass;
    private String newPass;
    private String email;

    public String getOldPass() {
        return oldPass != null ? oldPass : "";
    }

}
