package com.openclassrooms.configuration;

import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.NonNull;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.stream.Collectors;

public class YourCustomAuthoritiesConverter implements Converter<Jwt, Collection<GrantedAuthority>> {

    private final JwtGrantedAuthoritiesConverter defaultConverter = new JwtGrantedAuthoritiesConverter();

    @Override
    public Collection<GrantedAuthority> convert(@NonNull Jwt jwt) {
        Collection<GrantedAuthority> defaultAuthorities = defaultConverter.convert(jwt);
        if (defaultAuthorities == null) {
            defaultAuthorities = Collections.emptyList();
        }

        // Custom logic to extract authorities from JWT claims
        Map<String, Object> claims = jwt.getClaims();
        // Exemple: Extract authorities from a claim named "roles"
        Object rolesObj = claims.get("roles");
        if (rolesObj instanceof Collection<?>) {
            Collection<?> roles = (Collection<?>) rolesObj;
            return roles.stream()
                    .filter(role -> role instanceof String)
                    .map(role -> "ROLE_" + role) // Prefix each role with "ROLE_"
                    .map(SimpleGrantedAuthority::new)
                    .collect(Collectors.toList());
        }

        return defaultAuthorities;
    }
}
