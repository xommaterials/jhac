package com.sap.hybris.hac;

import org.junit.Test;

import java.io.InputStream;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;

public class ConfigurationTest {

  @Test
  public void defaultValues() {
    final Configuration configuration = Configuration.builder().build();
    assertThat(configuration.getEndpoint(), is("https://localhost:9002/hac"));
    assertThat(configuration.getUsername(), is("admin"));
    assertThat(configuration.getPassword(), is("nimda"));
    assertThat(configuration.getHtaccess(), is(nullValue()));
  }

  @Test
  public void fromFile() {
    final InputStream inputStream = getClass().getResourceAsStream("configuration.json");
    final Configuration configuration = Configuration.builder().from(inputStream).build();
    assertThat(configuration.getEndpoint(), is("endpoint"));
    assertThat(configuration.getUsername(), is("username"));
    assertThat(configuration.getPassword(), is("password"));
    assertThat(configuration.getHtaccess().getUsername(), is("htusername"));
    assertThat(configuration.getHtaccess().getPassword(), is("htpassword"));
  }

  @Test
  public void equals() {
    assertThat(Configuration.builder().build(), is(Configuration.builder().build()));
  }

  @Test
  public void hazhCode() {
    assertThat(
        Configuration.builder().build().hashCode(), is(Configuration.builder().build().hashCode()));
  }
}
