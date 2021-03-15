package com.sap.hybris.hac;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Builder;
import lombok.EqualsAndHashCode;
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
@Getter
@EqualsAndHashCode(callSuper=true)
public class Configuration extends Credentials {

  private String endpoint;
  private Credentials htaccess;

  private Configuration() {}

  @Builder
  public Configuration(
      final String endpoint,
      final String username,
      final String password,
      final Credentials htaccess) {
    super(username, password);
    this.endpoint = endpoint;
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
      }
      if (username == null) {
        username = "admin";
      }
      if (password == null) {
        password = "nimda";
      }

      // build
      return new Configuration(endpoint, username, password, htaccess);
    }
  }
  
}
