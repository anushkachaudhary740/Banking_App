package com.cognologix.BankSystemApplicationAssignment;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@SpringBootApplication
@Log4j2
public class BankSystemApplicationAssignment {

	public static void main(String[] args) {
		SpringApplication.run(BankSystemApplicationAssignment.class, args);
		log.info("Started..............................");
	}
	@Bean
	public ModelMapper modelMapper(){
		return new ModelMapper();
	}
}
