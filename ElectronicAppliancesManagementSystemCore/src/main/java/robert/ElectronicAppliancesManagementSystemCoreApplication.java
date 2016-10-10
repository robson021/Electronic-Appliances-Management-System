package robert;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableAutoConfiguration
@EntityScan(basePackages = "robert.db.entity")
@EnableJpaRepositories
public class ElectronicAppliancesManagementSystemCoreApplication {

	public static void main(String[] args) {
		SpringApplication.run(ElectronicAppliancesManagementSystemCoreApplication.class, args);
	}
}
