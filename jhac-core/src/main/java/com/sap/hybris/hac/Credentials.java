package com.sap.hybris.hac;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Builder(builderMethodName = "_builder")
@Getter
@EqualsAndHashCode
public class Credentials {

  protected String username;
  protected String password;

  protected Credentials() {}

  public Credentials(final String username, final String password) {
    this.username = username;
    this.password = password;
  }
}
