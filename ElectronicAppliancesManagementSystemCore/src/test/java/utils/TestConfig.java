package utils;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import robert.web.filters.BasicAuthFilter;

@Configuration
public class TestConfig {

	@Bean
	@Primary
	public BasicAuthFilter basicAuthFilter() {
		return new NoAuthFilter();
	}
}
