package com.example.authserver;

import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.core.oidc.OidcScopes;
import org.springframework.security.oauth2.server.authorization.client.JdbcRegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.settings.AuthorizationServerSettings;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;

import javax.sql.DataSource;
import java.util.Set;
import java.util.UUID;

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
	JdbcUserDetailsManager jdbcUserDetailsManager(DataSource dataSource){
		 return new JdbcUserDetailsManager(dataSource);
	 }
	 @Bean
	ApplicationRunner usersRunner(PasswordEncoder pswEncoder, UserDetailsManager usrDetailsMgr){
		 return args -> {
			 var builder 
		 }
	 }
}
	