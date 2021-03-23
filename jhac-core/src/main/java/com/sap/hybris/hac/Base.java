package com.sap.hybris.hac;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.rholder.retry.RetryException;
import com.github.rholder.retry.Retryer;
import com.github.rholder.retry.RetryerBuilder;
import com.sap.hybris.hac.exception.CommunicationException;
import com.sap.hybris.hac.util.StatefulRestTemplate;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import static com.github.rholder.retry.StopStrategies.stopAfterAttempt;
import static java.util.Collections.singletonList;

/**
 * Base implementation for any endpoint providing shared functionality communicating with hac.
 *
 * @author Klaus Hauschild
 */
public abstract class Base<REQUEST, RESPONSE> {

  private static final ObjectMapper MAPPER = new ObjectMapper();

  private final Logger logger = LoggerFactory.getLogger(getClass());
  private final Configuration configuration;
  private final Class<?> responseType;

  protected Base(final Configuration configuration, final Class<?> responseType) {
    this.configuration = configuration;
    this.responseType = responseType;
  }

  protected Configuration configuration() {
    return configuration;
  }

  protected RESPONSE execute(final REQUEST request, final String path)
      throws CommunicationException {
    return execute(request, path, "/execute");
  }

  protected RESPONSE execute(final REQUEST request, final String path, final String action)
      throws CommunicationException {
    logger.debug("Execute {}{}: {}", configuration.getEndpoint(), path, request);

    final HttpHeaders requestHeaders = requestHeaders();
    final RestTemplate restTemplate = prepareRestTemplate(requestHeaders, path);

    final Retryer<RESPONSE> retryer =
        RetryerBuilder.<RESPONSE>newBuilder()
            .retryIfExceptionOfType(CommunicationException.class)
            .withStopStrategy(stopAfterAttempt(3))
            .build();
    try {
      return retryer.call(() -> execute(request, path, action, requestHeaders, restTemplate));
    } catch (final ExecutionException exception) {
      throw new RuntimeException(exception);
    } catch (final RetryException exception) {
      throw new CommunicationException("Communication error. Retires failed.", request, exception);
    }
  }

  private RESPONSE execute(
      final REQUEST request,
      final String path,
      final String action,
      final HttpHeaders requestHeaders,
      final RestTemplate restTemplate) {
    try {
      // prepare entity
      final HttpEntity<MultiValueMap<String, Object>> requestEntity =
          requestEntity(request, requestHeaders);

      // perform request
      final ResponseEntity<RESPONSE> response =
          (ResponseEntity<RESPONSE>)
              restTemplate.exchange(
                  String.format("%s/console%s%s", configuration.getEndpoint(), path, action),
                  HttpMethod.POST,
                  requestEntity,
                  responseType);

      // handle not successful responses (redirect for wrong credentials)
      final HttpStatus statusCode = response.getStatusCode();
      if (!statusCode.is2xxSuccessful()) {
        throw new RestClientException(String.format("Not successful: %s", statusCode));
      }

      // extract response
      final RESPONSE result = response.getBody();
      logger.debug("Result: {}", result);
      return result;
    } catch (final RestClientException exception) {
      throw new CommunicationException(
          String.format("Error while communicating with %s", path), request, exception);
    }
  }

  protected RestTemplate prepareRestTemplate(final HttpHeaders requestHeaders, final String path) {
    final RestTemplate restTemplate = new StatefulRestTemplate();
    authenticate(restTemplate);

    final String url = configuration.getEndpoint() + "/console" + path;
    final ResponseEntity<String> executePageResponse =
        restTemplate.exchange(url, HttpMethod.GET, new HttpEntity<>(requestHeaders), String.class);
    final String csrfToken = extractCsrfToken(executePageResponse);
    requestHeaders.add("X-CSRF-TOKEN", csrfToken);

    return restTemplate;
  }

  private void authenticate(final RestTemplate restTemplate) {
    final ResponseEntity<String> loginPageResponse =
        restTemplate.exchange(
            configuration.getEndpoint(),
            HttpMethod.GET,
            new HttpEntity<String>(requestHeaders()),
            String.class);
    final String csrfToken = extractCsrfToken(loginPageResponse);

    logger.debug("    CSRF: {}", csrfToken);
    logger.debug("    user: {}", configuration.getUsername());
    logger.debug("password: {}", configuration.getPassword().replaceAll(".", "*"));

    final MultiValueMap<String, String> loginRequest = new LinkedMultiValueMap<>();
    loginRequest.put("_csrf", singletonList(csrfToken));
    loginRequest.put("j_username", singletonList(configuration.getUsername()));
    loginRequest.put("j_password", singletonList(configuration.getPassword()));
    loginRequest.put("_spring_security_remember_me", singletonList("on"));
    final HttpHeaders headers = requestHeaders();
    restTemplate.postForEntity(
        configuration.getEndpoint() + "/j_spring_security_check",
        new HttpEntity<>(loginRequest, headers),
        String.class);
  }

  private String extractCsrfToken(final ResponseEntity<String> pageResponse) {
    final Document loginPage = Jsoup.parse(pageResponse.getBody());
    if (loginPage == null) {
      return null;
    }
    final Elements csrfInputs = loginPage.select("input[name=_csrf]");
    if (csrfInputs.isEmpty()) {
      return null;
    }
    return csrfInputs.first().val();
  }

  private HttpHeaders requestHeaders() {
    final HttpHeaders requestHeaders = new HttpHeaders();
    requestHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
    final Credentials htaccess = configuration.getHtaccess();
    if (htaccess != null) {
      requestHeaders.setBasicAuth(
          htaccess.getUsername(), htaccess.getPassword(), StandardCharsets.UTF_8);
    }
    return requestHeaders;
  }

  @SuppressWarnings("unchecked")
  private HttpEntity<MultiValueMap<String, Object>> requestEntity(
      final REQUEST request, final HttpHeaders requestHeaders) {
    final MultiValueMap<String, Object> requestMap = new LinkedMultiValueMap<>();
    MAPPER
        .convertValue(request, Map.class)
        .forEach((key, value) -> requestMap.put(key.toString(), singletonList(value)));
    return new HttpEntity<>(requestMap, requestHeaders);
  }
}
