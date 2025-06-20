package com.example.userservice.config;

import com.example.userservice.dao.UserDAOImpl;
import com.example.userservice.entity.User;
import com.example.userservice.entity.UserRole;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;

import java.util.*;

public class KeycloakJwtAuthenticationConverter implements Converter<Jwt, AbstractAuthenticationToken> {
    private final JwtGrantedAuthoritiesConverter converter = new JwtGrantedAuthoritiesConverter();
    private final UserDAOImpl userDAO;

    public KeycloakJwtAuthenticationConverter(UserDAOImpl userDAO) {
        this.userDAO = userDAO;
    }


    @Override
    public AbstractAuthenticationToken convert(Jwt source) {
        Collection<GrantedAuthority> authorities = new ArrayList<>(converter.convert(source));
        String username = source.getClaimAsString("preferred_username");
        createOrUpdateUserFromKeycloak(source);
        Optional<User> user = Optional.ofNullable(userDAO.findByUsername(username));
        user.ifPresent(
                u -> authorities.add(
                        new SimpleGrantedAuthority("ROLE_" + u.getRole().name()
                        )));
        return new JwtAuthenticationToken(source, authorities);
    }

    public void createOrUpdateUserFromKeycloak(Jwt jwt){
        String username = jwt.getClaimAsString("preferred_username");
        String email = jwt.getClaimAsString("email");
        String fullname = jwt.getClaimAsString("name");

        Map<String, Object> access = jwt.getClaim("resource_access");

        Map<String, Object> clientAccess = (Map<String, Object>) access.get("my-client");
        List<String> roles = (List<String>) clientAccess.get("roles");
        String role = roles.getFirst();

        if(role == null){
            role = "TEACHER";
        }

        User user = userDAO.findByUsername(username);
        if (user == null) {
            user = new User();
            user.setUsername(username);
            user.setEmail(email);
            user.setFullName(fullname);
            user.setRole(UserRole.valueOf(role.toUpperCase()));
            userDAO.createOrUpdate(user);
        } else {
            boolean updated = false;
            if (!email.equals(user.getEmail())) {
                user.setEmail(email);
                updated = true;
            }
            if (!fullname.equals(user.getFullName())) {
                user.setFullName(fullname);
                updated = true;
            }
            if (updated) {
                userDAO.createOrUpdate(user);
            }
        }
    }
}
