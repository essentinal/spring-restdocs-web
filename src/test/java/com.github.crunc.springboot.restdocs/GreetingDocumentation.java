package com.github.crunc.springboot.restdocs;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.http.ResponseEntity;
import org.springframework.restdocs.RestDocumentation;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;

@RunWith(SpringJUnit4ClassRunner.class)
@WebIntegrationTest(randomPort = true)
@SpringApplicationConfiguration(classes = { TestApp.class })
public class GreetingDocumentation {

    @Rule
    public final RestDocumentation restDocumentation = new RestDocumentation("target/generated-snippets");

    private final RestTemplateDocumentation docs = RestTemplateDocumentation.using(restDocumentation).build();

    @Value("${local.server.port}")
    private int port;

    @Autowired
    private WebApplicationContext ctx;

    private MockMvc mvc;

//    @Before
//    public void setup() {
//        mvc = MockMvcBuilders.webAppContextSetup(ctx)
//                .apply(MockMvcRestDocumentation.documentationConfiguration(docs))
//                .build();
//    }

    @Test
    public void sample() throws Exception {


        docs.document("getGreetings",
                responseFields(
                        fieldWithPath("[].message").description("The greeting message")
                )
        ).getForEntity("http://localhost:" + port + "/greetings", Greeting[].class);

        final ResponseEntity<Greeting[]> res = docs.document("getGreetings")
                .responseField(fieldWithPath("[].message").description("The greeting message"))
                .get("http://localhost:" + port + "/greetings")
                .forEntity(Greeting[].class);

//        mvc.perform(get("/greetings"))
//                .andExpect(status().isOk())
//                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
//                .andDo(document("getGreetings",
//                        responseFields(
//                                fieldWithPath("[].message").description("The greeting message")
//                        )
//                ));
    }


}
