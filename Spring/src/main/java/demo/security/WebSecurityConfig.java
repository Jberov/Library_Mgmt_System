package demo.security;

import java.util.Arrays;
import java.util.List;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationEventPublisher;
import org.springframework.security.authentication.DefaultAuthenticationEventPublisher;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;


//@ComponentScan(basePackages = "demo.*")
@EnableWebSecurity
@Configuration
public class WebSecurityConfig{

	@Bean
	UserDetailsManager userDetailsManager (DataSource dataSource) {
		JdbcUserDetailsManager jdbcUserDetailsManager = new JdbcUserDetailsManager(dataSource);
		jdbcUserDetailsManager.setUsersByUsernameQuery("SELECT username, password, enabled FROM users WHERE username = ?");
		jdbcUserDetailsManager.setAuthoritiesByUsernameQuery("SELECT u.username, r.role FROM users u " +
				"JOIN roles r ON u.role_id = r.role_id WHERE u.username = ?");
		return jdbcUserDetailsManager;
	}

	@Autowired
	public WebSecurityConfig() {
	}

	@Bean
	SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity, BasicAuthEntryPoint basicAuthEntryPoint) throws Exception {
		httpSecurity.cors().and().csrf().disable().formLogin().disable()
				.authorizeRequests()
				.antMatchers(HttpMethod.POST, "/api/v1/books").hasAuthority("ADMIN")
				.antMatchers(HttpMethod.GET, "/api/v1/books/*").hasAnyAuthority("ADMIN", "USER")
				.antMatchers(HttpMethod.GET, "/api/v1/users/byBook/*").hasAuthority("ADMIN")
				.antMatchers(HttpMethod.GET, "/api/v1/users/info/*").hasAuthority("ADMIN")
				.antMatchers(HttpMethod.PATCH, "/api/v1/books/rental/*").hasAnyAuthority("USER", "ADMIN")
				.antMatchers(HttpMethod.POST, "/api/v1/users").permitAll()
				.antMatchers(HttpMethod.GET, "/registrationConfirm").permitAll()
				.antMatchers(HttpMethod.PUT, "/api/v1/users/*").hasAnyAuthority("USER", "ADMIN")
				.antMatchers(HttpMethod.DELETE, "/api/v1/users/").hasAuthority("ADMIN")
				.antMatchers(HttpMethod.DELETE, "/api/v1/books/*").hasAuthority("ADMIN")
				.antMatchers(HttpMethod.PATCH, "/api/v1/books/return/*").hasAnyAuthority("USER", "ADMIN")
				.antMatchers(HttpMethod.DELETE, "/api/v1/users/*").hasAuthority("ADMIN")
				.antMatchers(HttpMethod.GET, "/api/v1/users/history").hasAnyAuthority("USER", "ADMIN")
				.antMatchers("/api/v1/users/recommendation").hasAnyAuthority("USER", "ADMIN")
				.antMatchers("api/v1/statistics/*").hasAuthority("ADMIN")

				.anyRequest()
				.authenticated()
				.and()
				.httpBasic()
				.authenticationEntryPoint(basicAuthEntryPoint).and()
				.logout()
				.logoutUrl("/logout");
		return httpSecurity.build();
	}
	
	@Bean
	CorsConfigurationSource corsConfigurationSource()  {
		CorsConfiguration corsConfiguration = new CorsConfiguration();
		corsConfiguration.setAllowedOrigins(List.of("http://localhost"));
		corsConfiguration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "PATCH", "DELETE"));
		corsConfiguration.setAllowCredentials(true);
		corsConfiguration.addAllowedHeader("*");
		UrlBasedCorsConfigurationSource urlBasedCorsConfigurationSource = new UrlBasedCorsConfigurationSource();
		urlBasedCorsConfigurationSource.registerCorsConfiguration("/**", corsConfiguration);
		return urlBasedCorsConfigurationSource;
	}

	@Bean
	AuthenticationEventPublisher authenticationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
		// required so that we can have authentication listeners
		return new DefaultAuthenticationEventPublisher(applicationEventPublisher);
	}
}




