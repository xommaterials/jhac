package com.sap.hybris.hac.util;

import org.apache.http.client.HttpClient;
import org.apache.http.protocol.HttpContext;
import org.springframework.http.HttpMethod;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;

import java.net.URI;

public class StatefulHttpComponentsClientHttpRequestFactory
    extends HttpComponentsClientHttpRequestFactory {

  private final HttpContext httpContext;

  public StatefulHttpComponentsClientHttpRequestFactory(
      HttpClient httpClient, HttpContext httpContext) {
    super(httpClient);
    this.httpContext = httpContext;
  }

  @Override
  protected HttpContext createHttpContext(HttpMethod httpMethod, URI uri) {
    return this.httpContext;
  }
}
