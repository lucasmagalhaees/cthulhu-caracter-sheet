package com.lucasbarbosa.cthulhu.character.generator.api;

import static com.lucasbarbosa.cthulhu.character.generator.driver.util.ApplicationUtils.getHttpHeaders;

import java.util.Collections;
import java.util.Map;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/** Cheap liveness check: answers without building a character, so clients can probe the service before asking for sheets. */
@RestController
public class HealthController {

  @GetMapping(value = "/health", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Map<String, String>> health() {
    return ResponseEntity.ok().headers(getHttpHeaders()).body(Collections.singletonMap("status", "UP"));
  }
}
