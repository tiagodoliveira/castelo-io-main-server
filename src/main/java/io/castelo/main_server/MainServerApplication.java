package io.castelo.main_server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class MainServerApplication extends SpringBootServletInitializer {

	public static void main(String[] args) {
		SpringApplication.run(MainServerApplication.class, args);
	}

}
