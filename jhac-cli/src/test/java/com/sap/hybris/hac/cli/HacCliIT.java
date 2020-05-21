package com.sap.hybris.hac.cli;

import org.junit.Test;

public class HacCliIT {

  @Test
  public void version() {
    HacCli.main(new String[] {"-v"});
  }

  @Test
  public void help() {
    HacCli.main(new String[] {"-h"});
  }
}
