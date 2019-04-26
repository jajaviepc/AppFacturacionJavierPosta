package com.jpaProject.spring.app;

import java.nio.file.Paths;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class MvcConfig implements WebMvcConfigurer {
//Clase para que guarde y busque las imagenes en un directorio externos y no dentro del proyecto como tal.
private final Logger log = LoggerFactory.getLogger(getClass());
/*
	Se va a dejar comentado porque se va a usar otro metodo de subida para las imagenes 
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		WebMvcConfigurer.super.addResourceHandlers(registry);
		//Metodo 1
		//registry.addResourceHandler("/uploads/**").addResourceLocations("file:/C:/Temp/uploads/");
		String resourcePath = Paths.get("uploads").toAbsolutePath().toUri().toString();
		log.info(resourcePath);
		registry.addResourceHandler("/uploads/**").addResourceLocations(resourcePath);
		
	}*/
	
	
}
