package com.dophuong.inventory_service;

import com.dophuong.inventory_service.entity.Inventory;
import com.dophuong.inventory_service.repository.InventoryRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class InventoryServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(InventoryServiceApplication.class, args);
	}

	@Bean
	public CommandLineRunner loadData(InventoryRepository inventoryRepository) {
		return args -> {
			inventoryRepository.save(Inventory.builder()
					.skuCode("iPhone 15 Pro")
					.quantity(50)
					.build());

			inventoryRepository.save(Inventory.builder()
					.skuCode("Samsung Galaxy S24 Ultra")
					.quantity(40)
					.build());

			inventoryRepository.save(Inventory.builder()
					.skuCode("MacBook Air M3")
					.quantity(30)
					.build());

			inventoryRepository.save(Inventory.builder()
					.skuCode("Sony WH-1000XM5")
					.quantity(100)
					.build());

			inventoryRepository.save(Inventory.builder()
					.skuCode("iPad Pro 12.9 M2")
					.quantity(25)
					.build());
		};
	}
}

