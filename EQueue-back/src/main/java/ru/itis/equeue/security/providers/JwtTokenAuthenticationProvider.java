package ru.itis.equeue.security.providers;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import ru.itis.equeue.security.authentication.JwtTokenAuthentication;
import ru.itis.equeue.security.details.UserDetailsImpl;

@Component
public class JwtTokenAuthenticationProvider implements AuthenticationProvider {

    @Value("${security.jwt.token.secret-key}")
    private String jwtSecret;

    @Autowired
    @Qualifier("customUserDetailsService")
    private UserDetailsService userDetailsService;


    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        JwtTokenAuthentication tokenAuthentication = (JwtTokenAuthentication) authentication;

        Claims body;
        try {
            System.out.println(tokenAuthentication.getName());
            body = Jwts.parser()
                    .setSigningKey(jwtSecret)
                    .parseClaimsJws(tokenAuthentication.getName())
                    .getBody();
        } catch (MalformedJwtException e) {
            e.printStackTrace();
            throw new AuthenticationServiceException("Invalid token");
        }

        UserDetailsImpl userDetails = (UserDetailsImpl) userDetailsService.loadUserByUsername(body.get("login").toString());
        if (userDetails != null) {
            tokenAuthentication.setUserDetails(userDetails);
            tokenAuthentication.setAuthenticated(true);
        } else {
            throw new BadCredentialsException("Incorrect Token");
        }

        return tokenAuthentication;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return JwtTokenAuthentication.class.equals(authentication);
    }
}
