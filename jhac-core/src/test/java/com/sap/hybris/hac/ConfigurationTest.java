package com.sap.hybris.hac;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import org.junit.Test;

public class ConfigurationTest {

  @Test
  public void defaultValuesTest() {
    final Configuration configuration = Configuration.builder().build();
    assertThat(configuration.getEndpoint(), is("https://localhost:9002/hac"));
    assertThat(configuration.getUsername(), is("admin"));
    assertThat(configuration.getPassword(), is("nimda"));
  }
}
