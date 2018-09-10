package com.github.essentinal.springboot.restdocs;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpResponse;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class DocumentingClientHttpResponse implements ClientHttpResponse {

    private final ClientHttpResponse response;
    private byte[] content;

    public DocumentingClientHttpResponse(final ClientHttpResponse response) {
        this.response = response;
    }

    public byte[] getBodyContent() throws IOException {
        if (content == null) {
            final ByteArrayOutputStream buffer = new ByteArrayOutputStream();
            final InputStream inputStream = response.getBody();

            StreamCopy.from(inputStream)
                    .to(buffer)
                    .copy()
                    .close();

            content = buffer.toByteArray();
        }
        return content;
    }

    @Override
    public InputStream getBody() throws IOException {
        return new ByteArrayInputStream(getBodyContent());
    }

    @Override
    public HttpStatus getStatusCode() throws IOException {
        return response.getStatusCode();
    }

    @Override
    public int getRawStatusCode() throws IOException {
        return response.getRawStatusCode();
    }

    @Override
    public String getStatusText() throws IOException {
        return response.getStatusText();
    }

    @Override
    public void close() {
        response.close();
    }

    @Override
    public HttpHeaders getHeaders() {
        return response.getHeaders();
    }
}
