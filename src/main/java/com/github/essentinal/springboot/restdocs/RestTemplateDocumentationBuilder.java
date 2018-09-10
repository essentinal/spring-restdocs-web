package com.github.essentinal.springboot.restdocs;

import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.restdocs.RestDocumentationContextProvider;

import java.util.ArrayList;
import java.util.List;

/**
 * Fluent builder for {@link RestTemplateDocumentation}
 */
public class RestTemplateDocumentationBuilder {

  private final RestDocumentationContextProvider restDocumentation;
  private final List<HttpMessageConverter<?>> messageConverters = new ArrayList<>();
  private ClientHttpRequestFactory requestFactory;
  private RestTemplateOperationRequestFactory operationRequestFactory;
  private RestTemplateOperationResponseFactory operationResponseFactory;

  public RestTemplateDocumentationBuilder(final RestDocumentationContextProvider restDocumentation) {

    if (restDocumentation == null) {
      throw new IllegalArgumentException("RestDocumentation must not be null");
    }

    this.restDocumentation = restDocumentation;
  }

  /**
   * Use the given REST documentation for documenting the requests and responses.
   */
  public static RestTemplateDocumentationBuilder using(final RestDocumentationContextProvider restDocumentation) {
    return new RestTemplateDocumentationBuilder(restDocumentation);
  }

  /**
   * Use the given factory for creating {@link org.springframework.http.client.ClientHttpRequest} instances.
   */
  public RestTemplateDocumentationBuilder clientHttpRequestFactory(final ClientHttpRequestFactory requestFactory) {
    this.requestFactory = requestFactory;
    return this;
  }

  /**
   * Add the given message converter.
   */
  public RestTemplateDocumentationBuilder addMessageConverter(final HttpMessageConverter<?> messageConverter) {
    if (messageConverter != null) {
      this.messageConverters.add(messageConverter);
    }
    return this;
  }

  /**
   * Add the given message converters.
   */
  public RestTemplateDocumentationBuilder addMessageConverters(final HttpMessageConverter<?>... messageConverters) {
    if (messageConverters != null) {
      for (final HttpMessageConverter<?> messageConverter : messageConverters) {
        addMessageConverter(messageConverter);
      }
    }
    return this;
  }

  /**
   * Add the given message converters.
   */
  public RestTemplateDocumentationBuilder addMessageConverters(final Iterable<HttpMessageConverter<?>> messageConverters) {
    if (messageConverters != null) {
      for (final HttpMessageConverter<?> messageConverter : messageConverters) {
        addMessageConverter(messageConverter);
      }
    }
    return this;
  }

  /**
   * Use the given factory for creating {@link org.springframework.restdocs.operation.OperationRequest} instances.
   */
  public RestTemplateDocumentationBuilder operationRequestFactory(final RestTemplateOperationRequestFactory operationRequestFactory) {
    this.operationRequestFactory = operationRequestFactory;
    return this;
  }

  /**
   * Use the given factory for creating {@link org.springframework.restdocs.operation.OperationResponse} instances.
   */
  public RestTemplateDocumentationBuilder operationResponseFactory(final RestTemplateOperationResponseFactory operationResponseFactory) {
    this.operationResponseFactory = operationResponseFactory;
    return this;
  }

  /**
   * Finish building and create a new instance.
   */
  public RestTemplateDocumentation build() {
    return new RestTemplateDocumentation(
      restDocumentation,
      requestFactory,
      messageConverters,
      operationRequestFactory != null ? operationRequestFactory : new RestTemplateOperationRequestFactory(),
      operationResponseFactory != null ? operationResponseFactory : new RestTemplateOperationResponseFactory()
    );
  }
}
