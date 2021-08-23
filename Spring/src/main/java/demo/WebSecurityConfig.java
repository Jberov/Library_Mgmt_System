package demo;

import com.sap.cloud.security.xsuaa.XsuaaServiceConfiguration;
import com.sap.cloud.security.xsuaa.XsuaaServiceConfigurationDefault;
import com.sap.cloud.security.xsuaa.token.authentication.XsuaaJwtDecoderBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.jwt.Jwt;

import com.sap.cloud.security.xsuaa.token.TokenAuthenticationConverter;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Collections;
import java.util.logging.Level;
import java.util.logging.Logger;


@ComponentScan(basePackages = "demo.*")
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true ,securedEnabled = true, jsr250Enabled = true)

public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private XsuaaServiceConfiguration xsuaaServiceConfiguration;

    @Autowired
    private Logger log;

    @Override
    protected void configure(HttpSecurity http) throws Exception{
        http.cors().and().csrf().disable();
        System.out.println("Security");
        log.log(Level.ALL,"Enters");
        http.sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                .antMatchers(HttpMethod.POST,"**/api/v1/books").hasAuthority("Admin")
                //.antMatchers(HttpMethod.GET,"**/api/v1/book/*").hasAuthority("Admin")
                //.antMatchers(HttpMethod.GET,"**/api/v1/users/byBook/*").hasAuthority("Admin")
                //.antMatchers(HttpMethod.GET,"**/api/v1/users/info/*").hasAuthority("Admin")
                //.antMatchers(HttpMethod.PATCH,"**/api/v1/books/rental/*").hasAuthority("User")
                //.antMatchers(HttpMethod.GET,"**/api/v1/books").authenticated()
                //.antMatchers(HttpMethod.DELETE,"**/api/v1/book/*").hasAuthority("Admin")
                //.antMatchers(HttpMethod.PATCH,"**/api/v1/books/return/*").hasAuthority("User")
                //.antMatchers(HttpMethod.GET,"**/api/v1/users/history/*").hasAuthority("User")
                .anyRequest().denyAll()
                .and()
                .oauth2ResourceServer()
                .jwt()
                .jwtAuthenticationConverter(getJwtAuthenticationConverter());



    }
    Converter<Jwt, AbstractAuthenticationToken> getJwtAuthenticationConverter(){
        System.out.println("Security");
        log.log(Level.ALL,"Enters");
        TokenAuthenticationConverter converter = new TokenAuthenticationConverter(xsuaaServiceConfiguration);
        converter.setLocalScopeAsAuthorities(true);
        return converter;
    }
    @Autowired
    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        System.out.println("Security");
        log.log(Level.ALL,"Enters");
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Collections.singletonList("*"));
        configuration.setAllowedMethods(Collections.singletonList("*"));
        configuration.setAllowedHeaders(Collections.singletonList("*"));
        configuration.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
    @Autowired
    @Bean
    JwtDecoder jwtDecoder() {

        System.out.println("Security");
        log.log(Level.ALL,"Enters");
        return new XsuaaJwtDecoderBuilder(xsuaaServiceConfiguration).build();
    }

    @Autowired
    @Bean
    XsuaaServiceConfigurationDefault config() {
        System.out.println("Security");
        log.log(Level.ALL,"Enters");
        return new XsuaaServiceConfigurationDefault();
    }
}


