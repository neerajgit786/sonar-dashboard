package com.dashboard.app.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

	@Bean
	public PasswordEncoder passwordEncoder() {
	    return new BCryptPasswordEncoder(); // strong hashing
	}
	
	@Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/login", "/css/**","/signup","/forgot-password","/doSignup", "/js/**").permitAll() // allow login & static
                .anyRequest().authenticated()
            ).csrf(csrf -> csrf.disable())
            .formLogin(form -> form
                .loginPage("/login")          // custom login page
                .loginProcessingUrl("/doLogin") // Spring Security handles POST here
                .defaultSuccessUrl("/dashboard", true) // redirect after login
                .permitAll()
            )
            .logout(logout -> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/login?logout")
                .permitAll()
            ).sessionManagement(session -> session
                    .invalidSessionUrl("/login?timeout")   // redirect when session is invalid/expired
                    .maximumSessions(1)
                    .expiredUrl("/login?expired")          // redirect when logged in elsewhere
                );;

        return http.build();
    }
}
