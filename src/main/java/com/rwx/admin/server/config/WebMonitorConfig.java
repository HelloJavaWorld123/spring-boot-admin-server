package com.rwx.admin.server.config;

import de.codecentric.boot.admin.server.config.AdminServerProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

/**
 * @author : RXK
 * Date : 2019/10/10 16:42
 * Desc: security 与 监控页面 配置
 */
@Configuration
public class WebMonitorConfig extends WebSecurityConfigurerAdapter {

	private String contextPath;

	private final AdminServerProperties adminServerProperties;

	public WebMonitorConfig(AdminServerProperties adminServerProperties) {
		this.adminServerProperties = adminServerProperties;
		this.contextPath = adminServerProperties.getContextPath();
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests()
				.antMatchers(contextPath + "/assets/**").permitAll()
				.antMatchers(contextPath + "/login").permitAll()
				.anyRequest().authenticated()
				.and()
				.formLogin().loginPage(contextPath + "/login")
				.and()
				.logout().logoutUrl(contextPath + "/logout")
				.and().httpBasic().and()
				.csrf().csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
				.ignoringAntMatchers(contextPath + "/instances", contextPath + "/actuator/**");

	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.inMemoryAuthentication().passwordEncoder(NoOpPasswordEncoder.getInstance()).withUser("admin").password("admin").roles("USER");
	}
}
