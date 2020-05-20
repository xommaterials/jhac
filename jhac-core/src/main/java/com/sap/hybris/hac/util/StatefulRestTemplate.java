package com.sap.hybris.hac.util;

import org.apache.http.client.CookieStore;
import org.apache.http.client.HttpClient;
import org.apache.http.client.protocol.ClientContext;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.ssl.SSLContextBuilder;
import org.springframework.web.client.RestTemplate;

import javax.net.ssl.SSLContext;

public class StatefulRestTemplate extends RestTemplate {

  private final HttpClient httpClient;
  private final CookieStore cookieStore;
  private final HttpContext httpContext;
  private final StatefulHttpComponentsClientHttpRequestFactory
      statefulHttpComponentsClientHttpRequestFactory;

  public StatefulRestTemplate() {
    super();
    try {
      org.apache.http.ssl.SSLContextBuilder sslContextBuilder = SSLContextBuilder.create();
      sslContextBuilder.loadTrustMaterial(new org.apache.http.conn.ssl.TrustSelfSignedStrategy());
      SSLContext sslContext = sslContextBuilder.build();
      org.apache.http.conn.ssl.SSLConnectionSocketFactory sslSocketFactory =
          new SSLConnectionSocketFactory(
              sslContext, new org.apache.http.conn.ssl.DefaultHostnameVerifier());
      httpClient = HttpClients.custom().setSSLSocketFactory(sslSocketFactory).build();
    } catch (final Exception exception) {
      throw new RuntimeException(exception);
    }
    cookieStore = new BasicCookieStore();
    httpContext = new BasicHttpContext();
    httpContext.setAttribute(ClientContext.COOKIE_STORE, getCookieStore());
    statefulHttpComponentsClientHttpRequestFactory =
        new StatefulHttpComponentsClientHttpRequestFactory(httpClient, httpContext);
    super.setRequestFactory(statefulHttpComponentsClientHttpRequestFactory);
  }

  public CookieStore getCookieStore() {
    return cookieStore;
  }
}
