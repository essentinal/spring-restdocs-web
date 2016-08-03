package com.github.crunc.springboot.restdocs;


import org.springframework.restdocs.RestDocumentationContext;
import org.springframework.restdocs.curl.CurlDocumentation;
import org.springframework.restdocs.http.HttpDocumentation;
import org.springframework.restdocs.snippet.RestDocumentationContextPlaceholderResolver;
import org.springframework.restdocs.snippet.Snippet;
import org.springframework.restdocs.snippet.StandardWriterResolver;
import org.springframework.restdocs.snippet.WriterResolver;
import org.springframework.restdocs.templates.StandardTemplateResourceResolver;
import org.springframework.restdocs.templates.TemplateEngine;
import org.springframework.restdocs.templates.mustache.MustacheTemplateEngine;

import java.util.Arrays;
import java.util.List;

public class RestTemplateDocumentationContext {

    private final RestDocumentationContext restDocumentationContext;
    private final WriterResolver writerResolver;
    private final TemplateEngine templateEngine;

    private final List<Snippet> defaultSnippets = Arrays.asList(CurlDocumentation.curlRequest(),
            HttpDocumentation.httpRequest(), HttpDocumentation.httpResponse());

    public RestTemplateDocumentationContext(
            final RestDocumentationContext restDocumentationContext,
            final WriterResolver writerResolver,
            final TemplateEngine templateEngine) {

        this.restDocumentationContext = restDocumentationContext;
        this.writerResolver = writerResolver;
        this.templateEngine = templateEngine;
    }

    public RestTemplateDocumentationContext(final RestDocumentationContext restDocumentationContext) {
        this(
                restDocumentationContext,
                new StandardWriterResolver(new RestDocumentationContextPlaceholderResolver(restDocumentationContext)),
                new MustacheTemplateEngine(new StandardTemplateResourceResolver())
        );
    }

    public RestDocumentationContext restDocumentationContext() {
        return restDocumentationContext;
    }

    public WriterResolver writerResolver() {
        return writerResolver;
    }

    public TemplateEngine templateEngine() {
        return templateEngine;
    }

    public List<Snippet> defaultSnippets() {
        return defaultSnippets;
    }
}
