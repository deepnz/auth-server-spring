package com.example.authserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.server.authorization.settings.AuthorizationServerSettings;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

@SpringBootApplication
public class AuthServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(AuthServerApplication.class, args);
	}

	@Bean
	UserDetailsService inMemoryUserDetailsManager() {
		var userBuilder = User.builder();
		return new InMemoryUserDetailsManager(
				userBuilder.roles("USER").username("deepak").password("{bcrypt}$2a$10$h0JjKkXogaQTGlUwQb7RcOAe4.J6/d9WuJyqRiQxE27stv6icQwz2").build(),
				userBuilder.roles("USER","ADMIN").username("admin").password("{bcrypt}$2a$10$OWmNC3FmLftgkST51PZ5LOj04NgPONkah3Pik0b/v7CWvJcdIBI/O").build()
		);
	}
	@Bean
	public AuthorizationServerSettings authorizationServerSettings() {
		return AuthorizationServerSettings.builder()
				.build();
	}
}

	