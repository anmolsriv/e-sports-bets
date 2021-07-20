package net.esportsbets.config;

import javax.sql.DataSource;

import net.esportsbets.service.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
	@Autowired
	private DataSource dataSource;

	@Autowired
	private UserDetailsService userDetailsService;
	
	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Bean
	public DaoAuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
		authProvider.setUserDetailsService(userDetailsService);
		authProvider.setPasswordEncoder(passwordEncoder());
		
		return authProvider;
	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.authenticationProvider(authenticationProvider());
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests()
			.antMatchers("/").authenticated()
			.antMatchers("/signup_form").permitAll()
			.antMatchers("/process_register").permitAll()
			.antMatchers("/register").permitAll()
			.antMatchers("/register_success").permitAll()
			.antMatchers("/images/***").permitAll()
			.antMatchers("/webjars/bootstrap/css/***").permitAll()
			.antMatchers("/webjars/jquery/***").permitAll()
			.antMatchers("/webjars/bootstrap/css/***").permitAll()
			.antMatchers("/js/***").permitAll()
			.anyRequest().authenticated()
			.and()
			.formLogin()
				.usernameParameter("email")
				.defaultSuccessUrl("/players")
				.permitAll()
			.and()
			.logout().logoutSuccessUrl("/").permitAll();
	}
	
	
}
