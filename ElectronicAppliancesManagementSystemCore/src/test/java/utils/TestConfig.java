package utils;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Primary;
import robert.svc.api.MailService;

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

}
