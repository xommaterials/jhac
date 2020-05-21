package com.sap.hybris.hac.scripting;

import org.junit.Test;

public class ScriptTest {

  @Test(expected = IllegalArgumentException.class)
  public void nullScript() {
    Script.builder().script(null).build().validate();
  }

  @Test(expected = IllegalArgumentException.class)
  public void nullScriptType() {
    Script.builder().script("").scriptType(null).build().validate();
  }
}
