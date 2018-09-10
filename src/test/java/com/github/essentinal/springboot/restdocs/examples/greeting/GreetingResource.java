package com.github.essentinal.springboot.restdocs.examples.greeting;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

@RestController
public class GreetingResource {

  private final List<Greeting> greetings = Arrays.asList(
    new Greeting(1, "Hello REST Docs"),
    new Greeting(2, "Hello Again")
  );

  @GetMapping(value = "/greetings")
  public ResponseEntity<List<Greeting>> getGreetings() {
    return new ResponseEntity<>(
      greetings,
      HttpStatus.OK
    );
  }

  @PostMapping(value = "/greeting")
  public ResponseEntity<Greeting> postGreeting(@RequestBody Greeting greeting) {
    return new ResponseEntity<>(
      greeting,
      HttpStatus.OK
    );
  }
}
