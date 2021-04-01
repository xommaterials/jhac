package com.sap.hybris.hac;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;

/**
 * Client configuration. Default values are aligned for local development:
 *
 * <ul>
 *   <li>endpoint - https://localhost:9002/hac
 *   <li>username - admin
 *   <li>password - nimda
 * </ul>
 *
 * @author Klaus Hauschild
 */
@Getter
@EqualsAndHashCode(callSuper = true)
public class Configuration extends Credentials {

  private static final Logger LOGGER = LoggerFactory.getLogger(HybrisAdministrationConsole.class);

  private String endpoint;
  private Integer timeout;
  private Credentials htaccess;

  private Configuration() {}

  @Builder
  public Configuration(
      final String endpoint,
      final String username,
      final String password,
      final Integer timeout,
      final Credentials htaccess) {
    super(username, password);
    this.endpoint = endpoint;
    this.timeout = timeout;
    this.htaccess = htaccess;
  }

  public static class ConfigurationBuilder {

    public ConfigurationBuilder from(final InputStream inputStream) {
      final ObjectMapper objectMapper = new ObjectMapper();
      try {
        final Configuration configuration =
            objectMapper.readValue(inputStream, Configuration.class);
        endpoint = configuration.endpoint;
        username = configuration.username;
        password = configuration.password;
        timeout = configuration.timeout;
        htaccess = configuration.htaccess;
        return this;
      } catch (final IOException exception) {
        throw new IllegalArgumentException("unable to read configuration", exception);
      }
    }

    public Configuration build() {
      // default values
      if (endpoint == null) {
        endpoint = "https://localhost:9002/hac";
      } else {
        if (endpoint.endsWith("/")) {
          endpoint = endpoint.substring(0, endpoint.length() - 1);
        }
        if (!endpoint.endsWith("hac")) {
          LOGGER.warn(
              String.format("Typically hAC endpoint ands with /hac, your is: %s", endpoint));
        }
      }
      if (username == null) {
        username = "admin";
      }
      if (password == null) {
        password = "nimda";
      }
      if (timeout == null) {
        timeout = 0;
      }

      // build
      return new Configuration(endpoint, username, password, timeout, htaccess);
    }
  }
}
