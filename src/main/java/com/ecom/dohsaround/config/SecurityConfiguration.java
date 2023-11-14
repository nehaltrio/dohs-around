package com.ecom.dohsaround.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity

public class SecurityConfiguration {

        @Autowired
        AuthenticationSuccessHandlerClass authenticationSuccessHandlerClass;

        @Bean
        public UserDetailsService userDetailsServiceAdmin() {
                return new AdminServiceConfig();
        }

        @Bean
        public UserDetailsService userDetailsServiceCustomer() {
                return new CustomerServiceConfig();
        }

        @Bean
        public BCryptPasswordEncoder passwordEncoderAdmin() {
                return new BCryptPasswordEncoder();
        }

        @Bean
        public SecurityFilterChain filterChainAdmin(HttpSecurity http) throws Exception {
                AuthenticationManagerBuilder authenticationManagerBuilder = http
                                .getSharedObject(AuthenticationManagerBuilder.class);

                authenticationManagerBuilder
                                .userDetailsService(userDetailsServiceAdmin())
                                .passwordEncoder(passwordEncoderAdmin());

                authenticationManagerBuilder
                                .userDetailsService(userDetailsServiceCustomer())
                                .passwordEncoder(passwordEncoderAdmin());

                AuthenticationManager authenticationManager = authenticationManagerBuilder.build();

                http
                                .csrf(AbstractHttpConfigurer::disable)
                                .authorizeHttpRequests(author -> author
                                                .requestMatchers(PathRequest.toStaticResources().atCommonLocations())
                                                .permitAll()
                                                .requestMatchers("/**", "/js/**", "/css/**", "/images/**",
                                                                "/webfonts/**")
                                                .permitAll()
                                                .requestMatchers("/admin/**").hasAuthority("ADMIN")
                                                .requestMatchers("/customer/**").hasAuthority("CUSTOMER")
                                                .requestMatchers("/forgot-password", "/register", "/register-new")
                                                .permitAll()
                                                .anyRequest().authenticated())
                                .formLogin(login -> login.loginPage("/admin")
                                                .loginProcessingUrl("/admin-login")
                                                .successHandler(authenticationSuccessHandlerClass)
                                                .permitAll())
                                .logout(logout -> logout.invalidateHttpSession(true)
                                                .clearAuthentication(true)
                                                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                                                .logoutSuccessUrl("/adminlogin?logout")
                                                .permitAll())
                                .authenticationManager(authenticationManager)
                                .sessionManagement(
                                                session -> session.sessionCreationPolicy(SessionCreationPolicy.ALWAYS));
                return http.build();
        }

}