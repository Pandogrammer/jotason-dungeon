package com.farguito;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class JotasonDungeonApplication {

	public static String URL = "https://jotason-dungeon.herokuapp.com";
	//public static String URL = "http://localhost:8080";
	
	public static void main(String[] args) {
		SpringApplication.run(JotasonDungeonApplication.class, args);
	}
	
	
}

