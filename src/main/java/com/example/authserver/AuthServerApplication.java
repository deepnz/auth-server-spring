package com.example.authserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
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
				userBuilder.roles("USER").username("deepak").password("{bcrypt}$2a$10$O3C2zBwYsU94RJhyCBE1xuh97JlBfndTnEcU/3i1MOI8CLGx8Qmzy").build(),
				userBuilder.roles("USER","ADMIN").username("dagod").password("{bcrypt}$2a$10$amBbkdtq6Efbqek8u/Dfou9.Ws.tbbJgESMnDR57OvZ3Xme7kzCmu").build()
		);
	}
	@Bean
	public AuthorizationServerSettings authorizationServerSettings() {
		return AuthorizationServerSettings.builder()
				.build();
	}
}

	