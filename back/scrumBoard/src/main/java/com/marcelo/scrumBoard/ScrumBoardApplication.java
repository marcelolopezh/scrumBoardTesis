package com.marcelo.scrumBoard;

import java.util.TimeZone;

import javax.annotation.PostConstruct;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.marcelo.scrumBoard.security.JWTAuthorizationFilter;

@SpringBootApplication
public class ScrumBoardApplication {
	@PostConstruct
    void started() {
      TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
    }
	public static void main(String[] args) {
		SpringApplication.run(ScrumBoardApplication.class, args);
	}

	@EnableWebSecurity
	@Configuration
	class WebSecurityConfig extends WebSecurityConfigurerAdapter {

		@Override
		protected void configure(HttpSecurity http) throws Exception {

			http.csrf().disable()
					.addFilterAfter(new JWTAuthorizationFilter(), UsernamePasswordAuthenticationFilter.class)
					.authorizeRequests().antMatchers(HttpMethod.OPTIONS, "/**").permitAll()
					.antMatchers(HttpMethod.POST, "/createUser").permitAll()
					.antMatchers(HttpMethod.POST, "/login").permitAll()
					.antMatchers(HttpMethod.GET, "/getAllProjects").permitAll()
					.antMatchers(HttpMethod.GET, "/test1").permitAll()
					.antMatchers(HttpMethod.GET, "/pokemon-api").permitAll()
					.anyRequest().authenticated();
			http.cors().and();
		}
	}
}
