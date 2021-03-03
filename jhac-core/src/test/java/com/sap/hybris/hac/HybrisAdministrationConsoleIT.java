package com.sap.hybris.hac;

import com.sap.hybris.hac.exception.ConnectionException;
import org.junit.Test;

public class HybrisAdministrationConsoleIT {

  @Test(expected = ConnectionException.class)
  public void connectionTestFails() {
    HybrisAdministrationConsole.hac(
        Configuration.builder() //
            .endpoint("http://www.google.com")
            .build());
  }
}
