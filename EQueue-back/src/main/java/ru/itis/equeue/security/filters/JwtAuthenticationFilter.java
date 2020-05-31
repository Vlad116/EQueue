package ru.itis.equeue.security.filters;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.filter.OncePerRequestFilter;
import ru.itis.equeue.security.authentication.JwtTokenAuthentication;
import ru.itis.equeue.security.details.UserDetailsImpl;
import ru.itis.equeue.security.details.UserDetailsServiceImpl;
import ru.itis.equeue.security.providers.JwtTokenProvider;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtTokenProvider tokenProvider;

    @Autowired
    @Qualifier("customUserDetailsService")
    private UserDetailsServiceImpl customUserDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            String jwt = request.getHeader("AUTH");

            if (tokenProvider.validateToken(jwt)) {
                String username = tokenProvider.getUsernameFromJwt(jwt);

                UserDetails userDetails = customUserDetailsService.loadUserByUsername(username);

                JwtTokenAuthentication tokenAuthentication = new JwtTokenAuthentication(jwt);
                tokenAuthentication.setUserDetails((UserDetailsImpl) userDetails);
                tokenAuthentication.setAuthenticated(true);

                SecurityContextHolder.getContext().setAuthentication(tokenAuthentication);
            }
        } catch (Exception ex) {
            logger.error("Could not set user authentication in security context", ex);
        }

        filterChain.doFilter(request, response);
    }
}
