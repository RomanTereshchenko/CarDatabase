package com.foxminded.javaspring.cardb.security;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.core.DefaultOAuth2AuthenticatedPrincipal;
import org.springframework.security.oauth2.core.DelegatingOAuth2TokenValidator;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.OAuth2AuthenticatedPrincipal;
import org.springframework.security.oauth2.core.OAuth2TokenValidator;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtDecoders;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.BearerTokenAuthentication;
import org.springframework.security.web.SecurityFilterChain;

import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityScheme;

@Configuration
@EnableWebSecurity
@SecurityScheme(
    name = "Auth0",
    type = SecuritySchemeType.HTTP,
    bearerFormat = "JWT",
    scheme = "bearer"
)
public class TestSecurityConfig {

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http
				.authorizeHttpRequests(auth -> auth
				.anyRequest()
				.permitAll());
//				.requestMatchers("/swagger-ui/**", "swagger-resources/*", "/v3/api-docs/**").permitAll()
//				.anyRequest().hasAnyRole("USER", "MANAGER"))
//				.oauth2ResourceServer(oauth2ResourceServer -> oauth2ResourceServer
//				.jwt(jwt ->	jwt.decoder(jwtDecoder()).jwtAuthenticationConverter(jwtConverter())));
		return http.build();
	}
	
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().requestMatchers("/swagger-ui/**", "/bus/v3/api-docs/**");
    }
	
	private Converter<Jwt, ? extends AbstractAuthenticationToken> jwtConverter() {
		return (Converter<Jwt, AbstractAuthenticationToken>) jwt -> {
			OAuth2AccessToken accessToken = new OAuth2AccessToken(OAuth2AccessToken.TokenType.BEARER, jwt.getTokenValue(), 
					jwt.getIssuedAt(), jwt.getExpiresAt());
			Map<String, Object> attributes = jwt.getClaims();
			
			Collection<GrantedAuthority> authorities = new ArrayList<>();
			authorities.add(new SimpleGrantedAuthority("ROLE_" + attributes.get("user_role").toString()));
			
			OAuth2AuthenticatedPrincipal principal = new DefaultOAuth2AuthenticatedPrincipal(attributes, authorities);
			return new BearerTokenAuthentication(principal, accessToken, authorities);
		};
	}

	private JwtDecoder jwtDecoder() {
		OAuth2TokenValidator<Jwt> validator = new DelegatingOAuth2TokenValidator<>();

		NimbusJwtDecoder jwtDecoder = JwtDecoders.fromOidcIssuerLocation("https://dev-wsrrs20wnf6ersa3.us.auth0.com/");
		jwtDecoder.setJwtValidator(validator);
		return jwtDecoder;
	}
}
