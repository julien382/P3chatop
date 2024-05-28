package com.openclassrooms.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import com.nimbusds.jose.jwk.source.ImmutableSecret;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import javax.crypto.spec.SecretKeySpec;
@Configuration
@EnableWebSecurity
public class SpringSecurityConfig {
    @Value("${jwt.key}")
    private String jwtKey;
    @Bean
    public UserDetailsService userDetailsService() {
        // Retourner votre propre implémentation de UserDetailsService
        return new CustomUserDetailsService(); // Exemple: remplacer CustomUserDetailsService par votre classe réelle
    }
        /**
     * Custom Authentication manager.
     * @param userDetailsService Custom UserDetails Service.
     * @param passwordEncoder Custom password encoder.
     * @return AuthenticationManager
     */
    @Bean
    public AuthenticationManager authenticationManager(
            UserDetailsService userDetailsService,
            PasswordEncoder passwordEncoder) {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailsService);
        authenticationProvider.setPasswordEncoder(passwordEncoder);
                return new ProviderManager(authenticationProvider);
    }
    /**
     * Filter chain with no authentication required and without frame options header, order 1.
     * @param http HttpSecurity object.
     * @return SecurityFilterChain
     * @throws Exception thrown when a filter chain error occurred.
     */
    @Bean
    @Order(1)
    public SecurityFilterChain noAuthFilterChainWithoutFrameOptions(HttpSecurity http) throws Exception {
        return http
                .csrf(csrf ->
                        csrf.ignoringRequestMatchers(AntPathRequestMatcher.antMatcher("/h2-console/**")))
                .headers(headers -> headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::disable))
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .securityMatcher(AntPathRequestMatcher.antMatcher("/h2-console/**"))
                .authorizeHttpRequests(auth -> {
                    auth.requestMatchers(AntPathRequestMatcher.antMatcher("/h2-console/**")).permitAll();
                })
                .build();
    }
        /**
     * Filter chain with no authentication required, order 2.
     * @param http HttpSecurity object.
     * @return SecurityFilterChain
     * @throws Exception thrown when a filter chain error occurred.
     */
    @Bean
    @Order(2)
    public SecurityFilterChain noAuthFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .securityMatcher("/actuator/**", "/auth/register", "/v3/api-docs/**", "/swagger-ui/**")
                .authorizeHttpRequests(auth -> {
                    auth.anyRequest().permitAll();
                })
                .build();
    }
    /**
     * Filter chain with basic authentication, order 3.
     * @param http HttpSecurity object.
     * @return SecurityFilterChain
     * @throws Exception thrown when a filter chain error occurred.
     */
    @Bean
    @Order(3)
    public SecurityFilterChain authDelegatedFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .securityMatcher("api/auth/login","api/auth/register")
                .authorizeHttpRequests(auth -> auth.anyRequest().permitAll())
                .build();
    }
    /**
     * Filter chain with jwt authentication, order 4.
     * @param http HttpSecurity object.
     * @return SecurityFilterChain
     * @throws Exception thrown when a filter chain error occurred.
     */
    @Bean
    @Order(4)
    public SecurityFilterChain oauthFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth.anyRequest().authenticated())
                .oauth2ResourceServer(oauth2 -> oauth2.jwt(Customizer.withDefaults()))
                .build();
    }
    /**
     * BCrypt password encoder.
     * @return BCryptPasswordEncoder
     */
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    /**
     * JWT decoder.
     * @return JwtDecoder
     */
    @Bean
    public JwtDecoder jwtDecoder() {
        SecretKeySpec secretKey
                = new SecretKeySpec(this.jwtKey.getBytes(), 0, this.jwtKey.getBytes().length, "RSA");
        return NimbusJwtDecoder.withSecretKey(secretKey).macAlgorithm(MacAlgorithm.HS256).build();
    }
    /**
     * JWT encoder.
     * @return JwtEncoder
     */
    @Bean
    public JwtEncoder jwtEncoder() {
        return new NimbusJwtEncoder(new ImmutableSecret<>(this.jwtKey.getBytes()));
    }
}