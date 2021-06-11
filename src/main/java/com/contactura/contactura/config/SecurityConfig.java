package com.contactura.contactura.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.contactura.contactura.service.CustomUserDetailService;

@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	
	@Autowired
	private CustomUserDetailService customUserDetailService;
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {

		http.csrf().disable()
		.authorizeRequests()
		.antMatchers(HttpMethod.OPTIONS, "/**")
		.permitAll()
		.anyRequest()
		.authenticated()
		.and()
		.httpBasic();
	
//		Quando a autenticação for em memória
//		http.authorizeRequests()
//		.anyRequest()
//		.authenticated()
//		.and()
//		.httpBasic()
//		.and()
//		.csrf().disable();
	}
	
	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(customUserDetailService).passwordEncoder(new BCryptPasswordEncoder());
	
//		Quando a autenticação for em memória
//		@Autowired
//		public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
//			auth.inMemoryAuthentication()
//			.withUser("ricardo").password("{noop}1234").roles("USER")
//			.and()
//			.withUser("admin").password("{noop}admin").roles("ADMIN");

	} 	
}
