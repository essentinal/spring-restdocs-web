package com.github.essentinal.springboot.restdocs;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.restdocs.generate.RestDocumentationGenerationException;
import org.springframework.restdocs.operation.OperationResponse;
import org.springframework.restdocs.operation.OperationResponseFactory;

import java.io.IOException;

public class RestTemplateOperationResponseFactory {

  private final OperationResponseFactory factory;

  public RestTemplateOperationResponseFactory(final OperationResponseFactory factory) {
    this.factory = factory;
  }

  public RestTemplateOperationResponseFactory() {
    this(new OperationResponseFactory());
  }

  public OperationResponse createOperationResponse(final ClientHttpResponse response) {

    try {
      final HttpStatus status = response.getStatusCode();
      final HttpHeaders headers = new HttpHeaders();
      final byte[] content;

      if (response instanceof DocumentingClientHttpResponse) {
        content = ((DocumentingClientHttpResponse) response).getBodyContent();
      } else {
        throw new IllegalStateException("can not extract body content from " + response.getClass().getName()
          + ", require " + DocumentingClientHttpResponse.class.getName());
      }

      return factory.create(status, headers, content);
    } catch (final IOException e) {
      throw new RestDocumentationGenerationException(e);
    }
  }

}
