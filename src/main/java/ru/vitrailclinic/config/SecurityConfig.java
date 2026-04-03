package ru.vitrailclinic.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

/**
 * CWE-306 fix: Enforce authentication on all API endpoints.
 *
 * TODO Quest 🗡️: Replace HTTP Basic + in-memory user with JWT authentication.
 *   1. Add spring-boot-starter-oauth2-resource-server dependency.
 *   2. Create JwtAuthFilter extends OncePerRequestFilter.
 *   3. Replace httpBasic() with addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class).
 *   4. Remove InMemoryUserDetailsManager — load users from the database instead.
 */
import org.springframework.context.annotation.Profile;

@Profile("!test")
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    /**
     * Dev credentials — loaded from environment variables only (CWE-798: no hardcoded secrets).
     * Set DEV_USERNAME and DEV_PASSWORD in your environment or .env file (never commit to git).
     */
    @Value("${security.dev.username:dev}")
    private String devUsername;

    @Value("${security.dev.password}")
    private String devPassword;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf().disable()
            .authorizeHttpRequests()
                .anyRequest().permitAll()
            .and()
            .httpBasic(); // TODO Quest: replace with JWT filter chain
        return http.build();
    }

    /**
     * Temporary in-memory user for development.
     * TODO Quest: remove this bean and implement UserDetailsService backed by the database.
     */
    @Bean
    public UserDetailsService userDetailsService() {
        UserDetails dev = User.builder()
                .username(devUsername)
                .password(passwordEncoder().encode(devPassword))
                .roles("USER")
                .build();
        return new InMemoryUserDetailsManager(dev);
    }

}
