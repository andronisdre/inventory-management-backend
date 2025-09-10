package se.vgregion.inventory_management_backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class InventoryManagementBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(InventoryManagementBackendApplication.class, args);
	}

}
