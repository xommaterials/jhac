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
  private Credentials credentials;

  private Configuration() {}

  private Configuration(final String endpoint, final Credentials credentials) {
    this.endpoint = endpoint;
    this.credentials = credentials;
  }

  public static class ConfigurationBuilder {

    public ConfigurationBuilder from(final InputStream inputStream) {
      final ObjectMapper objectMapper = new ObjectMapper();
      try {
        final Configuration configuration =
            objectMapper.readValue(inputStream, Configuration.class);
        endpoint = configuration.endpoint;
        credentials = configuration.credentials;
        return this;
      } catch (final IOException exception) {
        throw new IllegalArgumentException("unable to read configuration", exception);
      }
    }

    public Configuration build() {
      // default values
      if (endpoint == null) {
        endpoint = "https://localhost:9002/hac";
      }
      if (credentials == null) {
        credentials =
            Credentials.builder() //
                .username("admin") //
                .password("nimda") //
                .build();
      }

      // build
      return new Configuration(endpoint, credentials);
    }
  }

  @Builder
  @Getter
  public static class Credentials {

    private String username;
    private String password;

    private Credentials() {}

    private Credentials(final String username, final String password) {
      this.username = username;
      this.password = password;
    }
  }
}
