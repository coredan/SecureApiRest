/**
* SecureApiRestApplication is the main Spring Boot class
* with main method.
*
* @author  Daniel A. S.
* @version 0.0.1
* @since   2016-07-12 
*/
package com.das;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableAutoConfiguration
@ComponentScan
@SpringBootApplication
public class SecureApiRestApplication {

	public static void main(String[] args) {
		SpringApplication.run(SecureApiRestApplication.class, args);
		
	}
}
