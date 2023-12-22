package com.example.authserver;

import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.core.oidc.OidcScopes;
import org.springframework.security.oauth2.server.authorization.JdbcOAuth2AuthorizationConsentService;
import org.springframework.security.oauth2.server.authorization.JdbcOAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.client.JdbcRegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.settings.AuthorizationServerSettings;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;

import javax.sql.DataSource;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

@SpringBootApplication
public class AuthServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(AuthServerApplication.class, args);
	}

	@Bean
	public AuthorizationServerSettings authorizationServerSettings() {
		return AuthorizationServerSettings.builder()
				.build();
	}
}
	@Configuration
	class AuthorizationConfiguration{

		@Bean
		JdbcOAuth2AuthorizationConsentService jdbcOAuth2AuthorizationConsentService(
				JdbcOperations jdbcOperations, RegisteredClientRepository repository) {
			return new JdbcOAuth2AuthorizationConsentService(jdbcOperations, repository);
		}

		@Bean
		JdbcOAuth2AuthorizationService jdbcOAuth2AuthorizationService(
				JdbcOperations jdbcOperations, RegisteredClientRepository rcr) {
			return new JdbcOAuth2AuthorizationService(jdbcOperations, rcr);
		}
	}
@Configuration
class ClientConfiguration{
	@Bean
	RegisteredClientRepository registeredClientRepository(JdbcTemplate template) {
		return new JdbcRegisteredClientRepository(template);
	}

	@Bean
	ApplicationRunner clientsRunner(RegisteredClientRepository repository) {
		return args -> {
			var clientId = "app";
			if (repository.findByClientId(clientId) == null) {
				repository.save(
						RegisteredClient
								.withId(UUID.randomUUID().toString())
								.clientId(clientId)
								.clientSecret("{bcrypt}$2a$10$2.v1HhI.JGQuzC5/Uc0oFud82ufoLkH93ER8PvGmjKCWp1x41FooC")
								.clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
								.authorizationGrantTypes(grantTypes -> grantTypes.addAll(Set.of(
										AuthorizationGrantType.CLIENT_CREDENTIALS,
										AuthorizationGrantType.AUTHORIZATION_CODE,
										AuthorizationGrantType.REFRESH_TOKEN)))
								.redirectUri("http://127.0.0.1:8282/login/oauth2/code/spring")
								.scopes(scopes -> scopes.addAll(Set.of("user.read", "user.write", OidcScopes.OPENID)))
								.build()
				);
			}
		};
	}
}


@Configuration
class UsersConfiguration {
	@Bean
	JdbcUserDetailsManager jdbcUserDetailsManager(DataSource dataSource) {
		return new JdbcUserDetailsManager(dataSource);
	}

	@Bean
	ApplicationRunner usersRunner(UserDetailsManager usrDetailsMgr) {
		return args -> {
			var builder = User.builder().roles("USER");
			var users = Map.of("deepak", "{bcrypt}$2a$10$mn1vvnBqgbz4NybPUclXXOkaAEji9akjdaOCvr0rPSVGeZdVGo3Xa",
					"admin", "{bcrypt}$2a$10$CW5lAFw0e/whmlUHPnEjpe7PVwBAP6VqaQzqYjXWLBngdoz67MnVW");
			;
			users.forEach((username, password) -> {
				if (!usrDetailsMgr.userExists(username)) {
					var user = builder
							.username(username)
							.password(password)
							.build();
					usrDetailsMgr.createUser(user);
				}
			});
		};
	}
}

//		@Bean
//	UserDetailsService inMemoryUserDetailsManager(){
//		var userBuilder = User.builder();
//		return new InMemoryUserDetailsManager(
//				userBuilder.roles("USER").username("deepak").password("{bcrypt}$2a$10$yy6rxHgqy9HM8a6z3pYvQ.PcJRCsfK5Uu2WOLBZuxGD1nkf4ytIlu").build(),
//				userBuilder.roles("ADMIN").username("admin").password("{bcrypt}$2a$10$CW5lAFw0e/whmlUHPnEjpe7PVwBAP6VqaQzqYjXWLBngdoz67MnVW").build()
//		);
//	}


	