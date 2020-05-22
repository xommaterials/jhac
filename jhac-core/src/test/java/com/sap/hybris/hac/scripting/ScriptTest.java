package com.sap.hybris.hac.scripting;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import org.junit.Test;

public class ScriptTest {

  @Test(expected = IllegalArgumentException.class)
  public void validation() {
    Script.builder().script((String) null).build();
  }

  @Test
  public void defaultValues() {
    final Script script = Script.builder().script("script").build();
    assertThat(script.getScript(), is("script"));
    assertThat(script.getScriptType(), is(ScriptType.groovy));
    assertThat(script.isCommit(), is(false));
  }
}
