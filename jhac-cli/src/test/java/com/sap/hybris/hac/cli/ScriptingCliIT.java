package com.sap.hybris.hac.cli;

import org.junit.Test;

public class ScriptingCliIT {

  @Test
  public void groovyRocks() {
    HacCli.main(
        new String[] {"--debug", "src/test/resources/com/sap/hybris/hac/cli/groovyRocks.groovy"});
  }

  @Test
  public void fileNotFound() {
    HacCli.main(new String[] {"file/not/found"});
  }
}
