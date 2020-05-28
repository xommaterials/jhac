package com.sap.hybris.hac;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Builder;
import lombok.Getter;

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
@Builder
@Getter
public class Configuration {

  private String endpoint;
  private String username;
  private String password;

  private Configuration() {
  }

  private Configuration(final String endpoint, final String username, final String password) {
    this.endpoint = endpoint;
    this.username = username;
    this.password = password;
  }

  static class ConfigurationBuilder {

    Configuration from(final InputStream inputStream) {
      final ObjectMapper objectMapper = new ObjectMapper();
      try {
        return objectMapper.readValue(inputStream, Configuration.class);
      } catch (final IOException exception) {
        throw new IllegalArgumentException("unable to read configuration");
      }
    }

    Configuration build() {
      // default values
      if (endpoint == null) {
        endpoint = "https://localhost:9002/hac";
      }
      if (username == null) {
        username = "admin";
      }
      if (password == null) {
        password = "nimda";
      }

      // build
      return new Configuration(endpoint, username, password);
    }
  }
}
