package com.sap.hybris.hac;

import lombok.Builder;
import lombok.Getter;

@Builder(builderMethodName = "_builder")
@Getter
public class Credentials {

  protected String username;
  protected String password;

  protected Credentials() {}

  public Credentials(final String username, final String password) {
    this.username = username;
    this.password = password;
  }
}
