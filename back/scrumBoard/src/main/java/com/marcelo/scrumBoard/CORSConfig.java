package com.marcelo.scrumBoard;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CORSConfig {

  // .allowedOrigins("http://localhost:8081")
  // .allowedOrigins("http://168.232.167.174:8081")
  @Bean
  public WebMvcConfigurer CORSConfigurer() {
    return new WebMvcConfigurer() {
      @Override
      public void addCorsMappings(CorsRegistry registry) {
        registry
          .addMapping("/**")
          .allowedOrigins(
        	"http://168.232.167.58:8081",
            "http://localhost:8081"
          )
          .allowedHeaders("*")
          .allowCredentials(true)
          .allowedMethods("GET", "POST", "PUT", "DELETE");
      }
    };
  }
}
