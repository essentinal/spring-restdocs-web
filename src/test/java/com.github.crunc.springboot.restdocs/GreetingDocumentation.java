package com.github.crunc.springboot.restdocs;

import static org.assertj.core.api.Java6Assertions.assertThat;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.restdocs.RestDocumentation;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@WebIntegrationTest(randomPort = true)
@SpringApplicationConfiguration(classes = { TestApp.class })
public class GreetingDocumentation {

    @Rule
    public final RestDocumentation restDocumentation = new RestDocumentation("target/generated-snippets");

    private final RestTemplateDocumentation docs = RestTemplateDocumentation.using(restDocumentation).build();

    @Value("${local.server.port}")
    private int port;

    @Test
    public void sample() throws Exception {

        final ResponseEntity<Greeting[]> res = docs.document("getGreetings")
                .responseField(fieldWithPath("[].message").description("The greeting message"))
                .get("http://localhost:" + port + "/greetings")
                .forEntity(Greeting[].class);

        assertThat(res.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(res.getHeaders().getContentType()).isEqualTo(MediaType.valueOf("application/json;charset=UTF-8"));
        assertThat(res.getBody()).containsExactly(new Greeting("Hello REST Docs"));

    }


}
