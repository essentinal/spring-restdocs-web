package com.github.essentinal.springboot.restdocs;

import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.snippet.Snippet;
import org.springframework.web.client.RestOperations;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;


public class RestTemplateDocumentation {

  private final RestDocumentationContextProvider restDocumentation;
  private final ClientHttpRequestFactory requestFactory;
  private final List<HttpMessageConverter<?>> messageConverters;
  private final RestTemplateOperationRequestFactory operationRequestFactory;
  private final RestTemplateOperationResponseFactory operationResponseFactory;

  protected RestTemplateDocumentation(
    final RestDocumentationContextProvider restDocumentation,
    final ClientHttpRequestFactory requestFactory,
    final List<HttpMessageConverter<?>> messageConverters,
    final RestTemplateOperationRequestFactory operationRequestFactory,
    final RestTemplateOperationResponseFactory operationResponseFactory) {

    this.restDocumentation = restDocumentation;
    this.requestFactory = requestFactory;
    this.messageConverters = messageConverters;
    this.operationRequestFactory = operationRequestFactory;
    this.operationResponseFactory = operationResponseFactory;
  }

  public static RestTemplateDocumentationBuilder using(final RestDocumentationContextProvider restDocumentation) {
    return RestTemplateDocumentationBuilder.using(restDocumentation);
  }

  public DocumentingRestTemplateBuilder document(final String identifier) {
    return new DocumentingRestTemplateBuilder(
      restDocumentation,
      identifier,
      requestFactory,
      messageConverters,
      operationRequestFactory,
      operationResponseFactory
    );
  }

  public RestOperations document(final String identifier, final List<Snippet> snippets) {

    return new DocumentingRestTemplate(
      restDocumentation,
      new RestTemplateOperationDocumenter(
        operationRequestFactory,
        operationResponseFactory,
        identifier,
        snippets
      ),
      requestFactory,
      messageConverters
    );
  }

  public RestOperations document(final String identifier, final Snippet... snippets) {

    return document(identifier, snippets != null ? Arrays.asList(snippets) : Collections.emptyList());
  }

}
