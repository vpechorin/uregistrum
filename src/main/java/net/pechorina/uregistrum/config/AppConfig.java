package net.pechorina.uregistrum.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.datatype.joda.JodaModule;

@Configuration
public class AppConfig {
	@Bean
	public Module jodaModule() {
	  return new JodaModule();
	}
}
