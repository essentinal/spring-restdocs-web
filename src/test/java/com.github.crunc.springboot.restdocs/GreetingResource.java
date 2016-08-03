package com.github.crunc.springboot.restdocs;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

@RestController
public class GreetingResource {

    private final List<Greeting> greetings = Arrays.asList(
            new Greeting("Hello REST Docs")
    );

    @RequestMapping(
            method = GET,
            value = "/greetings"
    )
    public ResponseEntity<List<Greeting>> getGreetings() {
        return new ResponseEntity<List<Greeting>>(
                greetings,
                HttpStatus.OK
        );
    }
}
