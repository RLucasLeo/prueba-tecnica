package prueba_tecnica.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import prueba_tecnica.jwt.JwtFiltrodeAutenticacion;
import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, AuthenticationProvider authProvider, JwtFiltrodeAutenticacion jwtFiltrodeAutenticacion) throws Exception {
        return http
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(authRequest -> authRequest
                .requestMatchers("/sign-up",
                "/v3/api-docs/**",     // documentación de swagger 
                "/swagger-ui/**",       // archivos de swagger 
                "/swagger-ui.html",     // pagina de swagger 
                "/swagger-resources/**").permitAll()
                .anyRequest().authenticated())
            .sessionManagement(sessionManager -> sessionManager
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authenticationProvider(authProvider)
            .addFilterBefore(jwtFiltrodeAutenticacion, UsernamePasswordAuthenticationFilter.class)
            .build();
    }

}
