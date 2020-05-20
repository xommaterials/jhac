package com.sap.hybris.hac;

import lombok.Builder;
import lombok.Builder.Default;
import lombok.Getter;

@Builder
@Getter
public class Configuration {

  @Default private String endpoint = "https://localhost:9002/hac";
  @Default private String username = "admin";
  @Default private String password = "nimda";
}
