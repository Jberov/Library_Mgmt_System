package demo;

import java.util.Collections;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;


@ComponentScan(basePackages = "demo.*")
//h@Configuration
@EnableWebSecurity
//@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true, jsr250Enabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {


	@Autowired
	public WebSecurityConfig() {
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.cors().and().csrf().disable();
		/*http.sessionManagement()
				.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
				.and()
				.authorizeRequests()
				.antMatchers(HttpMethod.POST, "/api/v1/books").hasAuthority("Admin")
				.antMatchers(HttpMethod.GET, "/api/v1/books/*").hasAuthority("Admin")
				.antMatchers(HttpMethod.GET, "/api/v1/users/byBook/*").hasAuthority("Admin")
				.antMatchers(HttpMethod.GET, "/api/v1/users/info/*").hasAuthority("Admin")
				.antMatchers(HttpMethod.PATCH, "/api/v1/books/rental/*").hasAnyAuthority("User", "Admin")
				.antMatchers(HttpMethod.GET, "/api/v1/books").authenticated()
				.antMatchers(HttpMethod.DELETE, "/api/v1/books/*").hasAuthority("Admin")
				.antMatchers(HttpMethod.PATCH, "/api/v1/books/return/*").hasAnyAuthority("User", "Admin")
				.antMatchers(HttpMethod.DELETE, "/api/v1/users/*").hasAuthority("Admin")
				.antMatchers(HttpMethod.GET, "/api/v1/users/history").hasAnyAuthority("User", "Admin")
				.anyRequest().authenticated()
				.and()
				.oauth2ResourceServer()
				.jwt()
				.jwtAuthenticationConverter(getJwtAuthenticationConverter());

		 */
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
}




