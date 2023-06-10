package demo.security;

import java.util.Arrays;
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
		jdbcUserDetailsManager.setAuthoritiesByUsernameQuery("SELECT u.username, u.role FROM users u WHERE u.username = ?");
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
				.antMatchers(HttpMethod.GET, "/api/v1/books").fullyAuthenticated()
				.antMatchers(HttpMethod.POST, "/api/v1/users/").permitAll()
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
		CorsConfiguration corsConfiguration = new CorsConfiguration();
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




