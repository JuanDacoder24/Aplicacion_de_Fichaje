package com.example.gestionfichaje.security;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import static org.springframework.security.config.Customizer.withDefaults;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

//Esto indica que es una clase configruaion de spring
@Configuration
public class SecurityConfig {

    @Autowired
    // Esto es para romper el ciclo diciendole que lo cargue mas tarde
    @Lazy
    private JwtAuthFilter jwtAuthFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                .cors(withDefaults())
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                .requestMatchers(org.springframework.http.HttpMethod.OPTIONS, "/**").permitAll()
                .requestMatchers("/api/auth/login", "/api/auth/register").permitAll()
                .requestMatchers("/api/horarios").permitAll()
                .requestMatchers("/api/horarios/**").permitAll()
                .requestMatchers("/api/fichajes").hasAnyRole("ADMIN", "EMPLEADO", "RRHH")
                .requestMatchers("/api/fichajes/**").hasAnyRole("ADMIN", "EMPLEADO", "RRHH")
                .requestMatchers("/api/solicitudes/mis-solicitudes").hasAnyRole("EMPLEADO", "ADMIN", "RRHH")
                .requestMatchers(org.springframework.http.HttpMethod.POST, "/api/solicitudes").hasAnyRole("EMPLEADO", "ADMIN", "RRHH")
                .requestMatchers(org.springframework.http.HttpMethod.PUT,"/api/solicitudes/*/revisar").hasRole("ADMIN")
                .requestMatchers("/api/solicitudes/**").hasAnyRole("ADMIN", "EMPLEADO", "RRHH")
                .requestMatchers("/api/solicitudes").hasAnyRole("ADMIN", "EMPLEADO", "RRHH")
                .requestMatchers("/api/usuarios").hasRole("ADMIN")
                .requestMatchers("/api/usuarios/**").hasAnyRole("ADMIN", "EMPLEADO")
                .requestMatchers("/api/justificantes").hasAnyRole("ADMIN", "EMPLEADO", "RRHH")
                .requestMatchers("/api/justificantes/**").hasAnyRole("ADMIN", "EMPLEADO", "RRHH")
                .requestMatchers("/admin/**").hasRole("ADMIN")
                .requestMatchers("/api/fichajes/**").hasAnyRole("ADMIN", "EMPLEADO", "RRHH")
                .requestMatchers("/api/usuarios/**").hasAnyRole("ADMIN", "EMPLEADO")
                .anyRequest().authenticated())
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        configuration.setAllowedOrigins(List.of("http://localhost:4200"));

        // Métodos HTTP permitidos
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));

        // Cabeceras permitidas
        // configuration.setAllowedHeaders(List.of("Authorization", "Content-Type"));
        configuration.setAllowedHeaders(List.of("*"));

        // Permitir credenciales (cookies, cabeceras de autorización, etc.)
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        // Aplica esta configuración a todas las rutas (/**)
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
