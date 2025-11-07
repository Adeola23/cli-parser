package com.example.parser;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.example.parser.cookie.CookieApplication;

@SpringBootApplication
public class ParserApplication {

	public static void main(String[] args) {
		CookieApplication cookieApplication = SpringApplication.run(ParserApplication.class, args).getBean(CookieApplication.class);
		cookieApplication.run(args);
	}

}
