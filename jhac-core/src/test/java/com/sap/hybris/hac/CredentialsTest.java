package com.sap.hybris.hac;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class CredentialsTest {

  @Test
  public void equals() {
    assertThat(
        new Credentials("username", "password"), is(new Credentials("username", "password")));
  }

  @Test
  public void hazhCode() {
    assertThat(
        new Credentials("username", "password").hashCode(),
        is(new Credentials("username", "password").hashCode()));
  }
}
