package com.cognologix.BankSystemApplicationAssignment;
import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
@SpringBootApplication
public class BankSystemApplicationAssignment {

	public static void main(String[] args) {
		SpringApplication.run(BankSystemApplicationAssignment.class, args);
		System.out.println("Started..............................");
	}
	@Bean
	public ModelMapper modelMapper(){
		return new ModelMapper();
	}


}
