package org.tunilink.tunilink.Security;

import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import org.tunilink.tunilink.Service.UserService;

import java.util.List;

@Component
public class JwtToPrincipalConverter {
    private UserService userService;

    public UserPrincipal convert(DecodedJWT jwt){
        return UserPrincipal.builder()
                .userId((jwt.getSubject()))
                .username(jwt.getClaim("username").asString())
                .authorities(extractAuthoritiesFromClaim(jwt))
                .build();
    }

    private List<SimpleGrantedAuthority> extractAuthoritiesFromClaim(DecodedJWT jwt){
        var claim = jwt.getClaim("role");
        if(claim.isNull() || claim.isMissing()) return List.of();
        return claim.asList(SimpleGrantedAuthority.class);
    }
}
