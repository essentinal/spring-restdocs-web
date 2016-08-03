package com.github.crunc.springboot.restdocs;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

/**
 * Wrapper for {@link RestTemplate} which provides a fluent API.
 */
public class FluentRestTemplate {

    private final RestTemplate delegate;

    public FluentRestTemplate(final RestTemplate delegate) {
        this.delegate = delegate;
    }

    public Exchange exchange(final String url, final HttpMethod method) {
        return new Exchange(url, method);
    }

    public Exchange get(final String url) {
        return exchange(url, HttpMethod.GET);
    }

    public Exchange post(final String url) {
        return exchange(url, HttpMethod.POST);
    }

    public Exchange put(final String url) {
        return exchange(url, HttpMethod.PUT);
    }

    public Exchange delete(final String url) {
        return exchange(url, HttpMethod.DELETE);
    }

    public Exchange patch(final String url) {
        return exchange(url, HttpMethod.PATCH);
    }

    public Exchange head(final String url) {
        return exchange(url, HttpMethod.HEAD);
    }

    public Exchange options(final String url) {
        return exchange(url, HttpMethod.OPTIONS);
    }

    public Exchange trace(final String url) {
        return exchange(url, HttpMethod.TRACE);
    }

    public class Exchange {

        private final String url;
        private final HttpMethod method;
        private final Map<String, Object> urlVariables = new HashMap<String, Object>();
        private HttpEntity<?> requestEntity = HttpEntity.EMPTY;

        private Exchange(final String url, final HttpMethod method) {
            this.url = url;
            this.method = method;
        }

        public <T> ResponseEntity<T> forEntity(final Class<T> responseType) {
            return delegate.exchange(url, method, requestEntity, responseType, urlVariables);
        }

        public ResponseEntity<?> forEntity() {
            return delegate.exchange(url, method, requestEntity, Object.class, urlVariables);
        }

    }
}
