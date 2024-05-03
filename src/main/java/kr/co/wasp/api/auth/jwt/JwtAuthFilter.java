package kr.co.wasp.api.auth.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Optional;

public class JwtAuthFilter extends OncePerRequestFilter {
    private final JwtSimpleUserDetailsService jwtSimpleUserDetailsService;
    private final JwtSimpleUserService jwtSimpleUserService;

    public JwtAuthFilter(JwtSimpleUserDetailsService jwtSimpleUserDetailsService, JwtSimpleUserService jwtSimpleUserService) {
        this.jwtSimpleUserDetailsService = jwtSimpleUserDetailsService;
        this.jwtSimpleUserService = jwtSimpleUserService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authorizationHeader = request.getHeader("Authorization");

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String token = authorizationHeader.substring(7);

            if (jwtSimpleUserService.validate(token)) {
                Optional<String> optionalId = jwtSimpleUserService.parseToken("id", token);
                if (optionalId.isPresent()) {
                    String id = optionalId.get();
                    UserDetails userDetails = jwtSimpleUserDetailsService.loadUserByUsername(id);

                    if (userDetails != null) {
                        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                                new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

                        SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
                    }
                }
            }
        }

        filterChain.doFilter(request, response);
    }
}
