package com.sadev.sharebook.configuration;

import com.sadev.sharebook.jwt.JwtFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;

@EnableWebSecurity
@Configuration
public class SecurityConfiguration {
    @Autowired
    JwtFilter jwtFilter;

    @Bean
    SecurityFilterChain web(HttpSecurity http) throws Exception {
        http
                //désactivation de la gestion des en-têtes CORS au sein de Spring Security
                .cors(request -> new CorsConfiguration().applyPermitDefaultValues())

                //désactivation du CSRF (Cross-Site Request Forgery)
                .csrf(AbstractHttpConfigurer::disable)

                //Configuration des règles d'autorisations concernant les requêtes HTTP
                .authorizeHttpRequests(auth -> auth
                        //authoriser le register et login
                        .requestMatchers("/users").permitAll()
                        .requestMatchers("/authenticate").permitAll()
                        .requestMatchers("/isConnected").permitAll()
                        .requestMatchers("/v3/api-docs/**").permitAll()
                        .requestMatchers("/api-docs.yaml").permitAll()
                        .requestMatchers("/swagger-resources/**").permitAll()
                        .requestMatchers("/swagger-ui/**").permitAll()
                        .requestMatchers("/swagger-ui.html").permitAll()
                        .requestMatchers("/webjars/**").permitAll()
                        .requestMatchers("/v3/api-docs/swagger-config").permitAll()
                        //Toutes les autres requêtes HTTP nécessitent une authentification
                        .anyRequest().authenticated()

                )

                // Configuration de la session Spring Security : AUCUNE session ne sera créée coté serveur
                // Moins coûteux et inutile lorsque nous sommes dans une configuration RESTful
                .sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                // Ajout d'un filtre personnalisé qui s'exécutera avant le filtre UsernamePasswordAuthenticationFilter
                // Filtre pour gérer l'authentification basée sur le JWT reçu dans les en-têtes de requêtes
                // Le filtre UsernamePasswordAuthenticationFilter est un filtre de base de Spring Security
                // Il est exécuté pour gérer l'authentification par username et mot de passe
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
