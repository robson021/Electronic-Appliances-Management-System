package robert.web.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import robert.web.filters.BasicAuthFilter;
import robert.web.filters.BasicAuthFilterImpl;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
				.authorizeRequests()
				//.antMatchers("/", "/home", "/index", "/login", "/logout", "/register").permitAll()
				.antMatchers("/**").permitAll();
		//.anyRequest().authenticated();
		// todo security
	}

	@Bean
	public BasicAuthFilter basicAuthFilter() {
		return new BasicAuthFilterImpl();
	}

}
