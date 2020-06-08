package com.tacs.rest;

import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import com.tacs.rest.security.JWTAuthorizationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


import java.io.IOException;
import java.net.URISyntaxException;
import java.util.*;


@SpringBootApplication
public class RestApplication {

    public static HashMap<String, Object> data = new HashMap<String, Object>();


    public static void main(String[] args) throws JsonIOException, JsonSyntaxException, IOException, URISyntaxException {
        SpringApplication.run(RestApplication.class, args);
    }

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**").allowedMethods("GET", "POST", "PUT", "DELETE", "PATCH");
            }
        };
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    //
    //Esto de abajo hace que todas las consultas que hagamos requieran el token como authentication menos
    //la del /session
    @EnableWebSecurity
    @Configuration
    class WebSecurityConfig extends WebSecurityConfigurerAdapter {


        @Autowired
        AuthenticationEntryPoint jwtAuthenticationEntryPoint;

        @Override
        protected void configure(HttpSecurity http) throws Exception {

            http.csrf().disable()
                    .addFilterAfter(new JWTAuthorizationFilter(), UsernamePasswordAuthenticationFilter.class)
                    .authorizeRequests()
                    .antMatchers(HttpMethod.POST, "/session").permitAll()
                    .antMatchers(HttpMethod.POST, "/users").permitAll()
                    .antMatchers(HttpMethod.POST, "/countriesList/prueba").permitAll()
                    .anyRequest().authenticated().and().
                    exceptionHandling().authenticationEntryPoint(jwtAuthenticationEntryPoint).and().sessionManagement()
                    .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        }
    }

    

}
