package com.foxminded.javaspring.cardb.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@EnableWebMvc
@Configuration
@EnableWebSecurity
public class SecSecurityConfig {

	@Bean
	public InMemoryUserDetailsManager userDetailsService() {
		InMemoryUserDetailsManager inMemoryUserDetailsManager = new InMemoryUserDetailsManager();
		UserDetails user = User.withUsername("user").password(passwordEncoder().encode("111")).roles("USER").build();
		inMemoryUserDetailsManager.createUser(user);
		UserDetails manager = User.withUsername("manager").password(passwordEncoder().encode("000")).roles("MANAGER").build();
		inMemoryUserDetailsManager.createUser(manager);
		
		return inMemoryUserDetailsManager;
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http
        .csrf(csrf -> csrf.disable())
        .authorizeHttpRequests(auth -> auth
        .requestMatchers("/login**")
        .permitAll()
        .anyRequest()
        .authenticated())
        .formLogin(form -> form
    	.loginPage("/login")
    	.permitAll());
//        .loginPage("/login.html")
//        .loginProcessingUrl("/perform_login")
//        .defaultSuccessUrl("/homepage.html", true)
//        .failureUrl("/login.html?error=true")
//        .failureHandler(authenticationFailureHandler())
//        .and()
//        .logout()
//        .logoutUrl("/perform_logout")
//        .deleteCookies("JSESSIONID")
//        .logoutSuccessHandler(logoutSuccessHandler();
        return http.build();
	}

}
