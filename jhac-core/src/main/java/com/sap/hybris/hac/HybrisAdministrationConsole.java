package com.sap.hybris.hac;

import com.sap.hybris.hac.flexiblesearch.FlexibleSearch;
import com.sap.hybris.hac.impex.Impex;
import com.sap.hybris.hac.scripting.Scripting;

public class HybrisAdministrationConsole {

  private final Configuration configuration;

  private HybrisAdministrationConsole(final Configuration configuration) {
    this.configuration = configuration;
  }

  public static HybrisAdministrationConsole hac() {
    return hac(Configuration.builder().build());
  }

  public static HybrisAdministrationConsole hac(final Configuration configuration) {
    return new HybrisAdministrationConsole(configuration);
  }

  public Scripting scripting() {
    return new Scripting(configuration);
  }

  public FlexibleSearch flexibleSearch() {
    return new FlexibleSearch(configuration);
  }

  public Impex impex() {
    return new Impex(configuration);
  }
}
