package utils;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Primary;
import robert.svc.api.MailService;
import robert.utils.api.AppLogger;

import java.io.File;

@Configuration
public class TestConfig {

    /*@Bean
    @Primary
    @Lazy
    public BasicAuthFilter basicAuthFilter() {
        return new BasicAuthFilter() {
            @Override
            public void init(FilterConfig filterConfig) throws ServletException {
            }

            @Override
            public void destroy() {
            }

            @Override
            public void doLogic(HttpServletRequest request, HttpServletResponse response) {
                // do nothing, no user auth in tests
                System.out.println("--- test filter run ---");
            }
        };
    }*/

    @Bean
    @Primary
    @Lazy
    public MailService mailService() {
        return new MailService() {
            @Override
            public void sendEmail(String receiverEmail, String topic, String message, File attachment) {
                System.out.println("--- test mailer run ---");
            }
        };
    }

    @Bean
    @Primary
    @Lazy
    public AppLogger appLogger() {
        return new AppLogger() {
            @Override
            public int getLoggingLevel() {
                return 0;
            }

            @Override
            public void info(Object... msg) {

            }

            @Override
            public void debug(Object... msg) {

            }

            @Override
            public void debug(Exception e) {

            }

            @Override
            public void warn(Object... msg) {

            }

            @Override
            public void error(Object... msg) {

            }

            @Override
            public void error(Exception e) {

            }
        };
    }

}
