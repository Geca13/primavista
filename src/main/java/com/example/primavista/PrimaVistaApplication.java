package com.example.primavista;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import com.example.primavista.production.entity.Operation;
import com.example.primavista.production.repository.OperationRepository;




@SpringBootApplication
public class PrimaVistaApplication implements WebMvcConfigurer {
	
	@Override
	public void addViewControllers(ViewControllerRegistry registry) {
		registry.addViewController("/employees").setViewName("allEmployees");
	}
	
	@Autowired
	OperationRepository operationRepository;

	public static void main(String[] args) {
		SpringApplication.run(PrimaVistaApplication.class, args);
	}
	
	@PostConstruct
	public void init() {
		
		try {
			
			operationRepository.save(new Operation(1, "Button Holles"));
			operationRepository.save(new Operation(2, "Button Placement"));
			operationRepository.save(new Operation(3, "Ironing"));
			operationRepository.save(new Operation(4, "Shtep"));
			operationRepository.save(new Operation(5, "Sostav"));
			operationRepository.save(new Operation(6, "Patent"));
			operationRepository.save(new Operation(7, "Opshivanje"));
			operationRepository.save(new Operation(8, "Kontola"));
			operationRepository.save(new Operation(9, "Pakuvanje"));
			
			} catch (Exception e) {
			System.err.println(e);
		}
	}
}
