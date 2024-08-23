package org.tunilink.tunilink.Security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.Instant;
import java.util.Collection;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class JwtIssuer {

    private final JwtProperties properties;

    public String issue( String username, Collection<? extends GrantedAuthority> roles) {
        // Serialize roles to a single string separated by a delimiter
        String rolesString = roles.stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(",")); // Use comma as a delimiter

        // Issue JWT token with claims
        return JWT.create()
                .withSubject(rolesString)
                .withExpiresAt(Instant.now().plus(Duration.ofDays(1)))
                .withClaim("username", username)
                .withClaim("role", rolesString) // Serialize roles as a single string
                .sign(Algorithm.HMAC256(properties.getSecretKey()));
    }


}
