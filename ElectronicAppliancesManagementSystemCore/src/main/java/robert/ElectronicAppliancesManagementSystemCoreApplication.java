package robert;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.core.task.TaskExecutor;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import static robert.enums.BeanNames.DEFAULT_TASK_EXECUTOR;

@SpringBootApplication
@EntityScan(basePackages = "robert.db.entity")
@EnableJpaRepositories
@EnableTransactionManagement
public class ElectronicAppliancesManagementSystemCoreApplication {

	@Bean(name = DEFAULT_TASK_EXECUTOR)
	public TaskExecutor taskExecutor() {
		ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
		executor.setCorePoolSize(8);
		executor.setQueueCapacity(3);
		executor.setMaxPoolSize(25);
		executor.setKeepAliveSeconds(30);

		return executor;
	}

	public static void main(String[] args) {
		SpringApplication.run(ElectronicAppliancesManagementSystemCoreApplication.class, args);
	}
}
