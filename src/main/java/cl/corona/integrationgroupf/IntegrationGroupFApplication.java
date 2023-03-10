package cl.corona.integrationgroupf;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class IntegrationGroupFApplication {

	public static void main(String[] args) {
		SpringApplication.run(IntegrationGroupFApplication.class, args);
	}

}
