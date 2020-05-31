package com.sap.hybris.hac;

import org.junit.Test;

import java.io.InputStream;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class ConfigurationTest {

  @Test
  public void defaultValues() {
    final Configuration configuration = Configuration.builder().build();
    assertThat(configuration.getEndpoint(), is("https://localhost:9002/hac"));
    assertThat(configuration.getCredentials().getUsername(), is("admin"));
    assertThat(configuration.getCredentials().getPassword(), is("nimda"));
  }

  @Test
  public void fromFile() {
    final InputStream inputStream = getClass().getResourceAsStream("configuration.json");
    final Configuration configuration = Configuration.builder().from(inputStream).build();
    assertThat(configuration.getEndpoint(), is("endpoint"));
    assertThat(configuration.getCredentials().getUsername(), is("username"));
    assertThat(configuration.getCredentials().getPassword(), is("password"));
  }
}
