package utils;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.task.TaskExecutor;
import robert.jobs.api.JobRegistration;
import robert.svc.api.MailService;
import robert.utils.api.AppLogger;

import static robert.enums.BeanNames.DEFAULT_TASK_EXECUTOR;

@Configuration
public class TestConfig {

    @Bean(name = DEFAULT_TASK_EXECUTOR)
    @Primary
    public TaskExecutor taskExecutor() {
        return runnable -> {
            System.out.println("--- mock task executor ---");
        };
    }

    @Bean
    @Primary
    public MailService mailService() {
        return (receiverEmail, topic, message, attachment) -> System.out.println("--- test mailer run ---");
    }

    @Bean
    @Primary
    public JobRegistration jobRegistration() {
        return strings -> {
        };
    }

    @Bean
    @Primary
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
