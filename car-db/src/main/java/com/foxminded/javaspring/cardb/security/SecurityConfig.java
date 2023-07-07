package com.foxminded.javaspring.cardb.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.oauth2.core.DelegatingOAuth2TokenValidator;
import org.springframework.security.oauth2.core.OAuth2TokenValidator;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtDecoders;
import org.springframework.security.oauth2.jwt.JwtValidators;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@EnableWebMvc
@Configuration
@EnableWebSecurity
public class SecurityConfig {
	
	@Value("${auth0.audience}")
	  private String audience;
	
	@Value("${spring.security.oauth2.resourceserver.jwt.issuer-uri}")
	  private String issuer;
	
	JwtDecoder jwtDecoder() {
	    OAuth2TokenValidator<Jwt> withAudience = new AudienceValidator(audience);
	    OAuth2TokenValidator<Jwt> withIssuer = JwtValidators.createDefaultWithIssuer(issuer);
	    OAuth2TokenValidator<Jwt> validator = new DelegatingOAuth2TokenValidator<>(withAudience, withIssuer);

	    NimbusJwtDecoder jwtDecoder = (NimbusJwtDecoder) JwtDecoders.fromOidcIssuerLocation(issuer);
	    jwtDecoder.setJwtValidator(validator);
	    return jwtDecoder;
	  }	

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http
        .csrf(AbstractHttpConfigurer::disable)
        .authorizeHttpRequests(auth -> auth
        .requestMatchers("/login**")
        .permitAll()
        .anyRequest()
        .authenticated())
        .oauth2ResourceServer(oauth2ResourceServer -> oauth2ResourceServer
        .jwt(jwt -> jwt.decoder(jwtDecoder())));
//        .oauth2Login(Customizer.withDefaults());
        return http.build();
	}
}
