package net.pechorina.uregistrum.config;

import net.pechorina.uregistrum.web.CustomObjectMapper;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.fasterxml.jackson.databind.ObjectMapper;

@Configuration
public class AppConfig {
	@Bean
	public ObjectMapper jacksonObjectMapper() {
	    ObjectMapper objectMapper = new CustomObjectMapper();
	    objectMapper.configure(com.fasterxml.jackson.databind.SerializationFeature.
	    	    WRITE_DATES_AS_TIMESTAMPS , false);
	    return objectMapper;
	}
}
