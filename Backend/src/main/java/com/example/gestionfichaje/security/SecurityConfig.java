package com.example.gestionfichaje.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

//Esto indica que es una clase configruaion de spring
@Configuration
public class SecurityConfig {

    @Bean
    //Aqui se define la seguridad http, que se protege y como
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
        
    }

}
