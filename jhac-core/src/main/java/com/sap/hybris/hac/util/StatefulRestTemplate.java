package com.sap.hybris.hac.util;

import org.apache.http.client.CookieStore;
import org.apache.http.client.HttpClient;
import org.apache.http.client.protocol.ClientContext;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.ssl.SSLContextBuilder;
import org.springframework.web.client.RestTemplate;

import javax.net.ssl.SSLContext;

/**
 * Stateful extension of {@link RestTemplate}.
 *
 * @author Klaus Hauschild
 */
public class StatefulRestTemplate extends RestTemplate {

  private final CookieStore cookieStore;

  public StatefulRestTemplate(final int timeout) {
    super();
    try {
      final SSLContextBuilder sslContextBuilder = SSLContextBuilder.create();
      sslContextBuilder.loadTrustMaterial(new TrustSelfSignedStrategy());
      final SSLContext sslContext = sslContextBuilder.build();
      final SSLConnectionSocketFactory sslSocketFactory =
          new SSLConnectionSocketFactory(sslContext, new NoopHostnameVerifier());
      final HttpClient httpClient =
          HttpClients.custom().setSSLSocketFactory(sslSocketFactory).build();

      cookieStore = new BasicCookieStore();
      final HttpContext httpContext = new BasicHttpContext();
      httpContext.setAttribute(ClientContext.COOKIE_STORE, getCookieStore());
      final StatefulHttpComponentsClientHttpRequestFactory requestFactory =
          new StatefulHttpComponentsClientHttpRequestFactory(httpClient, httpContext);
      requestFactory.setReadTimeout(timeout);
      requestFactory.setConnectTimeout(timeout);

      super.setRequestFactory(requestFactory);
    } catch (final Exception exception) {
      throw new RuntimeException(exception);
    }
  }

  public CookieStore getCookieStore() {
    return cookieStore;
  }
}
