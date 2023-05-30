package demo.security;

import java.util.Collections;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
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


@ComponentScan(basePackages = "demo.*")
@EnableWebSecurity
public class WebSecurityConfig{

	@Bean
	UserDetailsManager userDetailsManager (DataSource dataSource) {
		JdbcUserDetailsManager jdbcUserDetailsManager = new JdbcUserDetailsManager(dataSource);
		jdbcUserDetailsManager.setUsersByUsernameQuery("SELECT username, password FROM users WHERE username = ?");
		jdbcUserDetailsManager.setAuthoritiesByUsernameQuery("SELECT u.username, u.role FROM users u WHERE u.username = ?");
		// previously, one had to explicitly set the password encoder to BCryptPasswordEncoder
		// now, after WebSecurityConfigurerAdapter is deprecated, the PasswordEncoder is defined in a bean (in PasswordEncoderConfig)
		return jdbcUserDetailsManager;
	}

	@Autowired
	public WebSecurityConfig() {
	}

	@Bean
	SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
		httpSecurity.cors().and().csrf().disable().formLogin().disable();
		/*http.sessionManagement().authorizeRequests()
				.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
				.and()
				.authorizeRequests()
				.antMatchers(HttpMethod.POST, "/api/v1/books").hasAuthority("Admin")
				.antMatchers(HttpMethod.GET, "/api/v1/books/*").hasAuthority("Admin")
				.antMatchers(HttpMethod.GET, "/api/v1/users/byBook/*").hasAuthority("Admin")
				.antMatchers(HttpMethod.GET, "/api/v1/users/info/*").hasAuthority("Admin")
				.antMatchers(HttpMethod.PATCH, "/api/v1/books/rental/*").hasAnyAuthority("User", "Admin")
				.antMatchers(HttpMethod.GET, "/api/v1/books").authenticated()
				.antMatchers(HttpMethod.POST, "/api/v1/users/").anyRequest()
				.antMatchers(HttpMethod.PUT, "/api/v1/users/").hasAuthority("Admin")
				.antMatchers(HttpMethod.DELETE, "/api/v1/books/*").hasAuthority("Admin")
				.antMatchers(HttpMethod.PATCH, "/api/v1/books/return/*").hasAnyAuthority("User", "Admin")
				.antMatchers(HttpMethod.DELETE, "/api/v1/users/*").hasAuthority("Admin")
				.antMatchers(HttpMethod.GET, "/api/v1/users/history").hasAnyAuthority("User", "Admin")
				;

		 */
		return httpSecurity.build();
	}
	
	@Bean
	CorsConfigurationSource corsConfigurationSource() {
		CorsConfiguration configuration = new CorsConfiguration();
		configuration.setAllowedOrigins(Collections.singletonList("*"));
		configuration.setAllowedMethods(Collections.singletonList("*"));
		configuration.setAllowedHeaders(Collections.singletonList("*"));
		configuration.setAllowCredentials(true);
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", configuration);
		return source;
	}

	@Bean
	AuthenticationEventPublisher authenticationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
		// required so that we can have authentication listeners
		return new DefaultAuthenticationEventPublisher(applicationEventPublisher);
	}
}




