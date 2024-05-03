package kr.co.wasp.api.common.config;

import kr.co.wasp.api.auth.jwt.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true)
public class SecurityConfig {
    private static final String V1_API_PREFIX = "/api/v1/wasp";
    private final JwtSimpleUserDetailsService jwtSimpleUserDetailsService;
    private final JwtSimpleUserService jwtSimpleUserService;
    private final JwtSimpleUserAccessDeniedHandler accessDeniedHandler;
    private final JwtSimpleUserAuthenticationEntryPoint authenticationEntryPoint;

    private static final String[] WHITE_LIST = {V1_API_PREFIX + "/**"};

    public SecurityConfig(JwtSimpleUserDetailsService jwtSimpleUserDetailsService, JwtSimpleUserService jwtSimpleUserService, JwtSimpleUserAccessDeniedHandler accessDeniedHandler, JwtSimpleUserAuthenticationEntryPoint authenticationEntryPoint) {
        this.jwtSimpleUserDetailsService = jwtSimpleUserDetailsService;
        this.jwtSimpleUserService = jwtSimpleUserService;
        this.accessDeniedHandler = accessDeniedHandler;
        this.authenticationEntryPoint = authenticationEntryPoint;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.csrf(AbstractHttpConfigurer::disable);
        httpSecurity.cors(Customizer.withDefaults());

        httpSecurity.sessionManagement(sessionManagement -> sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        httpSecurity.formLogin(AbstractHttpConfigurer::disable);
        httpSecurity.httpBasic(AbstractHttpConfigurer::disable);

        httpSecurity.addFilterBefore(new JwtAuthFilter(jwtSimpleUserDetailsService, jwtSimpleUserService), UsernamePasswordAuthenticationFilter.class);

        httpSecurity.exceptionHandling((exceptionHandling) ->
                exceptionHandling
                        .authenticationEntryPoint(authenticationEntryPoint)
                        .accessDeniedHandler(accessDeniedHandler));

        httpSecurity.authorizeHttpRequests(authorize -> authorize.requestMatchers(WHITE_LIST).permitAll()
                .anyRequest().permitAll());

        return httpSecurity.build();
    }
}
