package com.github.essentinal.springboot.restdocs;

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
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class GreetingDocumentation {

  @Rule
  public final JUnitRestDocumentation restDocumentation = new JUnitRestDocumentation("target/generated-snippets");

  private final RestTemplateDocumentation docs = RestTemplateDocumentation.using(restDocumentation).build();

  @LocalServerPort
  private int port;

  @Test
  public void sample() {

    final ResponseEntity<Greeting[]> res = docs.document("getGreetings")
      .responseField(fieldWithPath("[].message").description("The greeting message"))
      .get("http://localhost:" + port + "/greetings")
      .forEntity(Greeting[].class);

    assertThat(res.getStatusCode()).isEqualTo(HttpStatus.OK);
    assertThat(res.getHeaders().getContentType()).isEqualTo(MediaType.valueOf("application/json;charset=UTF-8"));
    assertThat(res.getBody()).containsExactly(new Greeting("Hello REST Docs"));

  }


}
