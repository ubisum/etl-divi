package it.almaviva.mic.etl.divi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class EtlDiviApplication {

	public static void main(String[] args) {
		SpringApplication.run(EtlDiviApplication.class, args);
	}

}
