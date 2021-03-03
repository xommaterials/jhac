package com.sap.hybris.hac;

import com.sap.hybris.hac.exception.ConnectionException;
import com.sap.hybris.hac.flexiblesearch.FlexibleSearch;
import com.sap.hybris.hac.flexiblesearch.FlexibleSearchQuery;
import com.sap.hybris.hac.impex.ImportExport;
import com.sap.hybris.hac.scripting.Scripting;
import org.springframework.web.client.RestClientException;

/**
 * Hybris administration console.
 *
 * @author Klaus Hauschild
 */
public class HybrisAdministrationConsole {

  private final Configuration configuration;

  private HybrisAdministrationConsole(final Configuration configuration) {
    this.configuration = configuration;
    connectionTest();
  }

  /**
   * Performs a connection test before any 'real' request is performed.
   *
   * <p>This is achieved by executing a flexible search query that will never fail but returns also
   * nothing.
   *
   * <p>From that point on, any exception occurring while a request will be caused from
   * communication problems with the actual endpoint.
   *
   * @throws ConnectionException if connection test fails
   */
  private void connectionTest() throws ConnectionException {
    try {
      flexibleSearch()
          .query(
              FlexibleSearchQuery.builder()
                  .flexibleSearchQuery( //
                      "SELECT * " //
                          + "FROM { Product } " //
                          + "WHERE 0 = 1") //
                  .build());
    } catch (final RestClientException exception) {
      throw new ConnectionException(
          String.format(
              "Unable to establish connection to hAC at %s.", configuration.getEndpoint()),
          exception);
    }
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
