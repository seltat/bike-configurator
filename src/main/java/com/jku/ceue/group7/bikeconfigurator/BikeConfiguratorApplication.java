package com.jku.ceue.group7.bikeconfigurator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;


@SpringBootApplication
public class BikeConfiguratorApplication {

	private static final Logger log = LoggerFactory.getLogger(BikeConfiguratorApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(BikeConfiguratorApplication.class, args);
	}
}
