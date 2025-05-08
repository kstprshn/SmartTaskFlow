package ru.java.teamProject.SmartTaskFlow;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class SmartTaskFlowApplication {

	public static void main(String[] args) {
		SpringApplication.run(SmartTaskFlowApplication.class, args);
	}

}
