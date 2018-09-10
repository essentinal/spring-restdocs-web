package com.github.essentinal.springboot.restdocs.examples.greeting;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Greeting {

  private int number;

  private String message;
}
