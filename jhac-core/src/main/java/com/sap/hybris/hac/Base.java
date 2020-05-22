package com.sap.hybris.hac;

import static java.util.Collections.singletonList;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sap.hybris.hac.util.StatefulRestTemplate;
import java.util.Map;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

/**
 * Base implementation for any endpoint providing shared functionality communicating with hac.
 *
 * @author Klaus Hauschild
 */
public abstract class Base<REQUEST, RESPONSE> {

  private final ObjectMapper MAPPER = new ObjectMapper();

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

  protected RESPONSE execute(final REQUEST request, final String path) {
    return execute(request, path, "/execute");
  }

  protected RESPONSE execute(final REQUEST request, final String path, final String action) {
    logger.debug("Execute {}: {}", configuration.getEndpoint() + path, request);

    final HttpHeaders requestHeaders = requestHeaders();
    final RestTemplate restTemplate = prepareRestTemplate(requestHeaders, path);

    final HttpEntity<MultiValueMap<String, Object>> requestEntity =
        requestEntity(request, requestHeaders);
    final ResponseEntity<RESPONSE> response =
        (ResponseEntity<RESPONSE>)
            restTemplate.exchange(
                configuration.getEndpoint() + "/console" + path + action,
                HttpMethod.POST,
                requestEntity,
                responseType);

    final RESPONSE result = response.getBody();
    logger.debug("Result: {}", result);
    return result;
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
        restTemplate.getForEntity(configuration.getEndpoint(), String.class);
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
    return loginPage.select("input[name=_csrf]").first().val();
  }

  private HttpHeaders requestHeaders() {
    final HttpHeaders requestHeaders = new HttpHeaders();
    requestHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
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
