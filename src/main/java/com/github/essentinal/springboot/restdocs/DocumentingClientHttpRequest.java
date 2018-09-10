package com.github.essentinal.springboot.restdocs;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.client.ClientHttpRequest;
import org.springframework.http.client.ClientHttpResponse;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;

public class DocumentingClientHttpRequest implements ClientHttpRequest {

  private final ClientHttpRequest request;
  private final ByteArrayOutputStream bodyBuffer;

  public DocumentingClientHttpRequest(final ClientHttpRequest request) {
    this.request = request;
    this.bodyBuffer = new ByteArrayOutputStream(4096);
  }

  public byte[] getBodyContent() {
    return bodyBuffer.toByteArray();
  }

  @Override
  public OutputStream getBody() throws IOException {
    return new MaterializingOutputStream(request.getBody(), bodyBuffer);
  }

  @Override
  public ClientHttpResponse execute() throws IOException {
    return new DocumentingClientHttpResponse(request.execute());
  }

  @Override
  public HttpMethod getMethod() {
    return request.getMethod();
  }

  @Override
  public URI getURI() {
    return request.getURI();
  }

  @Override
  public HttpHeaders getHeaders() {
    return request.getHeaders();
  }

  @Override
  public String getMethodValue() {
    return request.getMethodValue();
  }

  private class MaterializingOutputStream extends OutputStream {

    private final OutputStream next;
    private final OutputStream buffer;

    private MaterializingOutputStream(final OutputStream next, final OutputStream buffer) {
      this.next = next;
      this.buffer = buffer;
    }

    @Override
    public void write(final int b) throws IOException {
      buffer.write(b);
      next.write(b);
    }

    @Override
    public void write(final byte[] bytes, final int i, final int i1) throws IOException {
      buffer.write(bytes, i, i1);
      next.write(bytes, i, i1);
    }
  }
}
