package com.github.crunc.springboot.restdocs;

import org.springframework.http.HttpMethod;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.restdocs.RestDocumentation;
import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.restdocs.payload.RequestFieldsSnippet;
import org.springframework.restdocs.payload.ResponseFieldsSnippet;
import org.springframework.restdocs.snippet.Snippet;

import java.util.ArrayList;
import java.util.List;

/**
 * Fluent builder for {@link DocumentingRestTemplate}
 */
public class DocumentingRestTemplateBuilder {

    private final RestDocumentation restDocumentation;
    private final String identifier;
    private final ClientHttpRequestFactory requestFactory;
    private final List<HttpMessageConverter<?>> messageConverters;
    private final RestTemplateOperationRequestFactory operationRequestFactory;
    private final RestTemplateOperationResponseFactory operationResponseFactory;

    private final List<FieldDescriptor> requestFields = new ArrayList<FieldDescriptor>();
    private final List<FieldDescriptor> responseFields = new ArrayList<FieldDescriptor>();
    private final List<Snippet> additionalSnippets = new ArrayList<Snippet>();

    public DocumentingRestTemplateBuilder(
            final RestDocumentation restDocumentation,
            final String identifier,
            final ClientHttpRequestFactory requestFactory,
            final List<HttpMessageConverter<?>> messageConverters,
            final RestTemplateOperationRequestFactory operationRequestFactory,
            final RestTemplateOperationResponseFactory operationResponseFactory) {

        this.restDocumentation = restDocumentation;
        this.identifier = identifier;
        this.requestFactory = requestFactory;
        this.messageConverters = messageConverters;
        this.operationRequestFactory = operationRequestFactory;
        this.operationResponseFactory = operationResponseFactory;
    }

    public DocumentingRestTemplateBuilder requestField(final FieldDescriptor fieldDescriptor) {
        if (fieldDescriptor != null) {
            requestFields.add(fieldDescriptor);
        }
        return this;
    }

    public DocumentingRestTemplateBuilder responseField(final FieldDescriptor fieldDescriptor) {
        if (fieldDescriptor != null) {
            responseFields.add(fieldDescriptor);
        }
        return this;
    }

    public DocumentingRestTemplateBuilder snippet(final Snippet snippet) {
        if (snippet != null) {
            additionalSnippets.add(snippet);
        }
        return this;
    }

    protected DocumentingRestTemplate build() {
        return new DocumentingRestTemplate(
                restDocumentation,
                new RestTemplateOperationDocumenter(
                        operationRequestFactory,
                        operationResponseFactory,
                        identifier,
                        buildSnippets()
                ),
                requestFactory,
                messageConverters
        );
    }

    private List<Snippet> buildSnippets() {

        final List<Snippet> finalSnippets = new ArrayList<Snippet>();

        if (!requestFields.isEmpty()) {
            finalSnippets.add(new RequestFieldsSnippet(requestFields) {
            });
        }

        if (!responseFields.isEmpty()) {
            finalSnippets.add(new ResponseFieldsSnippet(responseFields) {
            });
        }

        for (final Snippet snippet : additionalSnippets) {
            finalSnippets.add(snippet);
        }

        return finalSnippets;
    }

    public FluentRestTemplate.Exchange get(final String url) {
        return new FluentRestTemplate(build()).exchange(url, HttpMethod.GET);
    }

    public FluentRestTemplate.Exchange post(final String url) {
        return new FluentRestTemplate(build()).exchange(url, HttpMethod.POST);
    }

    public FluentRestTemplate.Exchange put(final String url) {
        return new FluentRestTemplate(build()).exchange(url, HttpMethod.PUT);
    }

    public FluentRestTemplate.Exchange delete(final String url) {
        return new FluentRestTemplate(build()).exchange(url, HttpMethod.DELETE);
    }

    public FluentRestTemplate.Exchange patch(final String url) {
        return new FluentRestTemplate(build()).exchange(url, HttpMethod.PATCH);
    }

    public FluentRestTemplate.Exchange head(final String url) {
        return new FluentRestTemplate(build()).exchange(url, HttpMethod.HEAD);
    }

    public FluentRestTemplate.Exchange options(final String url) {
        return new FluentRestTemplate(build()).exchange(url, HttpMethod.OPTIONS);
    }

    public FluentRestTemplate.Exchange trace(final String url) {
        return new FluentRestTemplate(build()).exchange(url, HttpMethod.TRACE);
    }
}
