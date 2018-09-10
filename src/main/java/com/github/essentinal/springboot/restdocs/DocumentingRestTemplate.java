package com.github.essentinal.springboot.restdocs;

import org.springframework.http.HttpMethod;
import org.springframework.http.client.ClientHttpRequest;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.util.Assert;
import org.springframework.web.client.RequestCallback;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.ResponseExtractor;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.net.URI;
import java.util.List;

public class DocumentingRestTemplate extends RestTemplate {

  private final RestDocumentationContextProvider restDocumentation;
  private final RestTemplateOperationDocumenter documenter;

  DocumentingRestTemplate(
    final RestDocumentationContextProvider restDocumentation,
    final RestTemplateOperationDocumenter documenter,
    final ClientHttpRequestFactory requestFactory,
    final List<HttpMessageConverter<?>> messageConverters) {

    this.restDocumentation = restDocumentation;
    this.documenter = documenter;

    if (requestFactory != null) {
      setRequestFactory(requestFactory);
    }

    if (messageConverters != null) {
      getMessageConverters().addAll(messageConverters);
    }
  }

  @Override
  protected <T> T doExecute(
    final URI url,
    final HttpMethod method,
    final RequestCallback requestCallback,
    final ResponseExtractor<T> responseExtractor) {

    Assert.notNull(url, "'url' must not be null");
    Assert.notNull(method, "'method' must not be null");

    ClientHttpResponse response = null;

    try {
      final ClientHttpRequest request = createRequest(url, method);

      if (requestCallback != null) {
        requestCallback.doWithRequest(request);
      }

      response = request.execute();
      handleResponse(url, method, response);

      documenter.document(url, method, request, response, new RestTemplateDocumentationContext(restDocumentation.beforeOperation()));

      if (responseExtractor != null) {
        return responseExtractor.extractData(response);
      } else {
        return null;
      }
    } catch (final IOException ex) {
      throw new ResourceAccessException("I/O error on " + method.name() +
        " request for \"" + url + "\": " + ex.getMessage(), ex);
    } finally {
      if (response != null) {
        response.close();
      }
    }
  }

  @Override
  protected ClientHttpRequest createRequest(final URI url, final HttpMethod method) throws IOException {
    return new DocumentingClientHttpRequest(super.createRequest(url, method));
  }
}
