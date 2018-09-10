package com.github.essentinal.springboot.restdocs;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;

@RestController
public class GreetingResource {

  private final List<Greeting> greetings = Collections.singletonList(
    new Greeting("Hello REST Docs")
  );

  @GetMapping(value = "/greetings")
  public ResponseEntity<List<Greeting>> getGreetings() {
    return new ResponseEntity<>(
      greetings,
      HttpStatus.OK
    );
  }
}
