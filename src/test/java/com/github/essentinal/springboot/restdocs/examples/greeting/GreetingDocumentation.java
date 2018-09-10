package com.github.essentinal.springboot.restdocs.examples.greeting;

import com.github.essentinal.springboot.restdocs.RestTemplateDocumentation;
import static org.assertj.core.api.Java6Assertions.assertThat;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.restdocs.JUnitRestDocumentation;
import org.springframework.restdocs.payload.FieldDescriptor;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class GreetingDocumentation {

  @Rule
  public final JUnitRestDocumentation restDocumentation = new JUnitRestDocumentation();

  private final RestTemplateDocumentation docs = RestTemplateDocumentation.using(restDocumentation).build();

  @LocalServerPort
  private int port;

  @Test
  public void documentPostGreeting() {

    final FieldDescriptor[] greeting = new FieldDescriptor[]{
      fieldWithPath("number").description("The index of the documentation"),
      fieldWithPath("message").description("The greeting message")
    };

    final ResponseEntity<Greeting> res = docs.document("postGreeting")
      .snippet(
        requestFields(greeting)
      )
      .snippet(
        responseFields(greeting)
      )
      .post("http://localhost:" + port + "/greeting")
      .withContent(new Greeting(1, "Greet"))
      .forEntity(Greeting.class);

    assertThat(res.getStatusCode()).isEqualTo(HttpStatus.OK);
    assertThat(res.getHeaders().getContentType()).isEqualTo(MediaType.valueOf("application/json;charset=UTF-8"));
    assertThat(res.getBody()).isEqualTo(new Greeting(1, "Greet"));

  }

  @Test
  public void documentGetGreetings() {

    final FieldDescriptor[] greeting = new FieldDescriptor[]{
      fieldWithPath("number").description("The index of the documentation"),
      fieldWithPath("message").description("The greeting message")
    };

    final ResponseEntity<Greeting[]> res = docs.document("getGreetings")
      .snippet(
        responseFields(fieldWithPath("[]")
          .description("An array of greetings"))
          .andWithPrefix("[].", greeting)
      )
      .get("http://localhost:" + port + "/greetings")
      .forEntity(Greeting[].class);

    assertThat(res.getStatusCode()).isEqualTo(HttpStatus.OK);
    assertThat(res.getHeaders().getContentType()).isEqualTo(MediaType.valueOf("application/json;charset=UTF-8"));
    assertThat(res.getBody()).containsExactly(
      new Greeting(1, "Hello REST Docs"),
      new Greeting(2, "Hello Again")
    );

  }


}
