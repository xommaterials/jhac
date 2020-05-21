package com.sap.hybris.hac;

import lombok.Builder;
import lombok.Builder.Default;
import lombok.Getter;

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

  @Default private String endpoint = "https://localhost:9002/hac";
  @Default private String username = "admin";
  @Default private String password = "nimda";
}
