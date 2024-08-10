package com.techlabs.app.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.techlabs.app.security.JwtAuthenticationEntryPoint;
import com.techlabs.app.security.JwtAuthenticationFilter;

@Configuration
public class SecurityConfiguration {

	private JwtAuthenticationEntryPoint authenticationEntryPoint;
	private JwtAuthenticationFilter authenticationFilter;

	public SecurityConfiguration(JwtAuthenticationEntryPoint authenticationEntryPoint, JwtAuthenticationFilter authenticationFilter) {
		this.authenticationEntryPoint = authenticationEntryPoint;
		this.authenticationFilter = authenticationFilter;
	}
	
	@Bean
	static PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
		return configuration.getAuthenticationManager();
	}
	
	@Bean
	SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
	    http.csrf(csrf -> csrf.disable())
	        .authorizeHttpRequests(authorize -> authorize
	            .requestMatchers("/swagger-ui/**", "/v3/api-docs/**", "/v3/api-docs.yaml", 
	                             "/swagger-resources/**", "/swagger-ui.html", "/webjars/**").permitAll()
	            .requestMatchers("/api/auth/**").permitAll()
	            .requestMatchers(HttpMethod.GET, "/api/customers/**").hasAuthority("ADMIN")
	            .requestMatchers(HttpMethod.GET, "/api/accounts/**").hasAuthority("ADMIN")
	            .requestMatchers(HttpMethod.GET, "/api/transactions/**").hasAuthority("ADMIN")
	            .requestMatchers(HttpMethod.POST, "/api/accounts/**").hasAuthority("ADMIN")
	            .requestMatchers(HttpMethod.PUT, "/api/accounts/**").hasAuthority("ADMIN")
	            .requestMatchers(HttpMethod.DELETE, "/api/accounts/**").hasAuthority("ADMIN")
	            .requestMatchers(HttpMethod.GET, "/api/myaccounts/**").hasAuthority("CUSTOMER")
	            .requestMatchers(HttpMethod.POST, "/api/mytransactions/**").hasAuthority("CUSTOMER")
	            .requestMatchers(HttpMethod.GET, "/api/mybalance/**").hasAuthority("CUSTOMER")
	            .anyRequest().authenticated()
	        )
	        .exceptionHandling(exception -> exception
	            .authenticationEntryPoint(authenticationEntryPoint)
	        )
	        .sessionManagement(session -> session
	            .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
	        );

	    http.addFilterBefore(authenticationFilter, UsernamePasswordAuthenticationFilter.class);
	    return http.build();
	}


}
