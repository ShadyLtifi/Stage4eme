package org.tunilink.tunilink.Entity;


import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
public class Otp {
    private String email;
    private String otp;
}
