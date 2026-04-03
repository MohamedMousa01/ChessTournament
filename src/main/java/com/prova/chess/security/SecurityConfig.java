package com.prova.chess.security;

import jakarta.servlet.DispatcherType;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity 
public class SecurityConfig  {

	final private JWTFilter jwtFilter;
	final private CustomUserDetailsService customUserDetailsService;
	final private JWTAuthEntryPoint unauthorizedHandler;
	final private CustomAccessDeniedHandler accessDeniedHandler;

    public SecurityConfig(JWTFilter jwtFilter, CustomUserDetailsService customUserDetailsService, JWTAuthEntryPoint unauthorizedHandler, CustomAccessDeniedHandler accessDeniedHandler) {
        this.jwtFilter = jwtFilter;
        this.customUserDetailsService = customUserDetailsService;
        this.unauthorizedHandler = unauthorizedHandler;
		this.accessDeniedHandler = accessDeniedHandler;
    }

    @Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http
				// Disable CSRF (not needed for stateless JWT)
				.csrf(csrf -> csrf.disable())

				// Configure endpoint authorization
				.authorizeHttpRequests(auth -> auth
						.dispatcherTypeMatchers(DispatcherType.ERROR, DispatcherType.FORWARD).permitAll()
						// Public endpoints
						.requestMatchers("/api/auth/login", "/api/auth/register").permitAll()

						// Role-based endpoints
						.requestMatchers("/api/utente/userInfo").authenticated()
						.requestMatchers("/api/tornei").hasAnyRole("ADMIN", "ORGANIZER")
						.requestMatchers("api/play").hasRole("PLAYER")
						.requestMatchers("/api/admin/**").hasRole("ADMIN")
				//		.requestMatchers("/**").hasAnyRole("ADMIN", "PLAYER", "ORGANIZER")

						// All other endpoints require authentication
						.anyRequest().authenticated()
				)

				// non abbiamo bisogno di una sessione: meglio forzare a stateless
				// Stateless session (required for JWT)
				.sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

				// Set custom authentication provider
				.authenticationProvider(authenticationProvider())

				// quando qualcosa fallisce ho il mio handler che customizza la response
				.exceptionHandling(configurer -> configurer
						.authenticationEntryPoint(unauthorizedHandler)
						.accessDeniedHandler(accessDeniedHandler)
				)

				// Adding the JWT filter
				.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);


		return http.build();
	}

	/*
	 * Authentication provider configuration
	 * Links UserDetailsService and PasswordEncoder
	 */
	@Bean
	public AuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
		provider.setUserDetailsService(this.customUserDetailsService);
		provider.setPasswordEncoder(passwordEncoder());
		return provider;
	}

	/*
	 * Authentication manager bean
	 * Required for programmatic authentication (e.g., in /generateToken)
	 */
	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
		return config.getAuthenticationManager();
	}

}
