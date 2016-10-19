package utils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Primary;

import robert.web.filters.BasicAuthFilter;

@Configuration
public class TestConfig {

    @Bean
    @Primary
    @Lazy
    public BasicAuthFilter basicAuthFilter() {
        return new BasicAuthFilter() {
            @Override
            public void doLogic(HttpServletRequest request, HttpServletResponse response) {
                // do nothing, no user auth in tests
            }
        };
    }

}
