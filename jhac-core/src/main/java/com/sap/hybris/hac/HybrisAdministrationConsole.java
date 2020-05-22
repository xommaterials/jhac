package com.sap.hybris.hac;

import com.sap.hybris.hac.flexiblesearch.FlexibleSearch;
import com.sap.hybris.hac.impex.ImportExport;
import com.sap.hybris.hac.scripting.Scripting;

/**
 * Hybris administration console.
 *
 * @author Klaus Hauschild
 */
public class HybrisAdministrationConsole {

  private final Configuration configuration;

  private HybrisAdministrationConsole(final Configuration configuration) {
    this.configuration = configuration;
  }

  /**
   * Create hac client with default configuration.
   *
   * @return hac client
   */
  public static HybrisAdministrationConsole hac() {
    return hac(Configuration.builder().build());
  }

  /**
   * Create hac client with given configuration
   *
   * @param configuration configuration
   * @return hac client
   */
  public static HybrisAdministrationConsole hac(final Configuration configuration) {
    return new HybrisAdministrationConsole(configuration);
  }

  /**
   * Scripting endpoint.
   *
   * @return scripting endpoint
   */
  public Scripting scripting() {
    return new Scripting(configuration);
  }

  /**
   * Flexible search endpoint.
   *
   * @return flexible search endpoint
   */
  public FlexibleSearch flexibleSearch() {
    return new FlexibleSearch(configuration);
  }

  /**
   * ImportExport endpoint.
   *
   * @return impex endpoint
   */
  public ImportExport impex() {
    return new ImportExport(configuration);
  }
}
