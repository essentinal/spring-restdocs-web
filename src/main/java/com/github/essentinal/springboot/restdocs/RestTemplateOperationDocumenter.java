package com.github.essentinal.springboot.restdocs;

import org.springframework.http.HttpMethod;
import org.springframework.http.client.ClientHttpRequest;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.restdocs.RestDocumentationContext;
import org.springframework.restdocs.generate.RestDocumentationGenerationException;
import org.springframework.restdocs.operation.Operation;
import org.springframework.restdocs.operation.OperationRequest;
import org.springframework.restdocs.operation.OperationResponse;
import org.springframework.restdocs.operation.StandardOperation;
import org.springframework.restdocs.operation.preprocess.OperationRequestPreprocessor;
import org.springframework.restdocs.operation.preprocess.OperationResponsePreprocessor;
import org.springframework.restdocs.snippet.Snippet;
import org.springframework.restdocs.snippet.WriterResolver;
import org.springframework.restdocs.templates.TemplateEngine;

import java.io.IOException;
import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RestTemplateOperationDocumenter {

  private final RestTemplateOperationRequestFactory operationRequestFactory;
  private final RestTemplateOperationResponseFactory operationResponseFactory;

  private final String identifier;

  private final OperationRequestPreprocessor requestPreprocessor;
  private final OperationResponsePreprocessor responsePreprocessor;

  private final List<Snippet> snippets;

  public RestTemplateOperationDocumenter(
    final RestTemplateOperationRequestFactory operationRequestFactory,
    final RestTemplateOperationResponseFactory operationResponseFactory,
    final String identifier,
    final OperationRequestPreprocessor requestPreprocessor,
    final OperationResponsePreprocessor responsePreprocessor,
    final List<Snippet> snippets) {

    this.operationRequestFactory = operationRequestFactory;
    this.operationResponseFactory = operationResponseFactory;
    this.identifier = identifier;
    this.requestPreprocessor = requestPreprocessor;
    this.responsePreprocessor = responsePreprocessor;
    this.snippets = snippets;
  }

  public RestTemplateOperationDocumenter(
    final RestTemplateOperationRequestFactory operationRequestFactory,
    final RestTemplateOperationResponseFactory operationResponseFactory,
    final String identifier,
    final List<Snippet> snippets) {

    this(
      operationRequestFactory,
      operationResponseFactory,
      identifier,
      IdentityOperationRequestPreprocessor.Instance,
      IdentityOperationResponsePreprocessor.Instance,
      snippets
    );
  }

  public RestTemplateOperationDocumenter document(
    final URI url,
    final HttpMethod method,
    final ClientHttpRequest req,
    final ClientHttpResponse res,
    final RestTemplateDocumentationContext context) {

    final Map<String, Object> attrs = new HashMap<>(4);
    attrs.put(RestDocumentationContext.class.getName(), context.restDocumentationContext());
    attrs.put(WriterResolver.class.getName(), context.writerResolver());
    attrs.put(TemplateEngine.class.getName(), context.templateEngine());

    final OperationRequest operationReq = requestPreprocessor.preprocess(operationRequestFactory.createOperationRequest(url, method, req));
    final OperationResponse operationRes = responsePreprocessor.preprocess(operationResponseFactory.createOperationResponse(res));
    final Operation operation = new StandardOperation(identifier, operationReq, operationRes, attrs);

    for (final Snippet snippet : context.defaultSnippets()) {
      try {
        snippet.document(operation);
      } catch (final IOException e) {
        throw new RestDocumentationGenerationException("failed to extract documentation", e);
      }
    }

    for (final Snippet snippet : snippets) {
      try {
        snippet.document(operation);
      } catch (final IOException e) {
        throw new RestDocumentationGenerationException("failed to extract documentation", e);
      }
    }

    return this;
  }

  private static class IdentityOperationRequestPreprocessor implements OperationRequestPreprocessor {

    static final OperationRequestPreprocessor Instance = new IdentityOperationRequestPreprocessor();

    private IdentityOperationRequestPreprocessor() {
      // private
    }

    @Override
    public OperationRequest preprocess(final OperationRequest request) {
      return request;
    }
  }

  private static class IdentityOperationResponsePreprocessor implements OperationResponsePreprocessor {

    static final OperationResponsePreprocessor Instance = new IdentityOperationResponsePreprocessor();

    private IdentityOperationResponsePreprocessor() {
      // private
    }

    @Override
    public OperationResponse preprocess(final OperationResponse response) {
      return response;
    }
  }
}

