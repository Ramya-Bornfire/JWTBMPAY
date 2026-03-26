package com.bornfire;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.client.RestTemplate;



@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
@ComponentScan(basePackages = "com.bornfire")
public class MerchantApplication extends SpringBootServletInitializer {
	public static void main(String[] args) {
		SpringApplication.run(MerchantApplication.class, args);
	}
	@Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
	
}
