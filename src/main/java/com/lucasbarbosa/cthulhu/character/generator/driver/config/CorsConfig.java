package com.lucasbarbosa.cthulhu.character.generator.driver.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Lets the browser call the API from other sites (the cthulhu-front and the Secret Hitler game, each on its own Vercel
 * domain). The origins come from CORS_ORIGINS, comma separated; by default any origin may read (the API is public and
 * read-only).
 */
@Configuration
public class CorsConfig implements WebMvcConfigurer {

  @Value("${cors.origins:*}")
  private String origins;

  @Override
  public void addCorsMappings(CorsRegistry registry) {
    registry.addMapping("/**").allowedOriginPatterns(origins.split("\\s*,\\s*")).allowedMethods("GET", "OPTIONS");
  }
}
