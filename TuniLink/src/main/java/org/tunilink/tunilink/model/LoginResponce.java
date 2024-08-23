package org.tunilink.tunilink.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LoginResponce {
    private String accessToken;


    public LoginResponce(String accessToken) {
        this.accessToken = accessToken;

    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }
}
