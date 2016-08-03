package com.github.crunc.springboot.restdocs;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.client.ClientHttpRequest;
import org.springframework.restdocs.operation.OperationRequest;
import org.springframework.restdocs.operation.OperationRequestFactory;
import org.springframework.restdocs.operation.OperationRequestPart;
import org.springframework.restdocs.operation.Parameters;

import java.net.URI;
import java.util.Collections;
import java.util.List;

public class RestTemplateOperationRequestFactory {

    private final OperationRequestFactory factory;

    public RestTemplateOperationRequestFactory(final OperationRequestFactory factory) {
        this.factory = factory;
    }

    public RestTemplateOperationRequestFactory() {
        this(new OperationRequestFactory());
    }

    public OperationRequest createOperationRequest(final URI url, final HttpMethod method, final ClientHttpRequest request) {

        final HttpHeaders headers = new HttpHeaders();
        final Parameters params = new Parameters();
        final List<OperationRequestPart> parts = Collections.emptyList();
        final byte[] body;

        if (request instanceof DocumentingClientHttpRequest) {
            body = ((DocumentingClientHttpRequest) request).getBodyContent();
        } else {
            throw new IllegalStateException("can not extract body content from " + request.getClass().getName()
                    + ", require " + DocumentingClientHttpRequest.class.getName());
        }

        return factory.create(url, method, body, headers, params, parts);
    }
}