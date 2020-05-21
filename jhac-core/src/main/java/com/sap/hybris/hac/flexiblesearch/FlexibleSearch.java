package com.sap.hybris.hac.flexiblesearch;

import com.sap.hybris.hac.Base;
import com.sap.hybris.hac.Configuration;

/**
 * Flexible search endpoint.
 *
 * @author Klaus Hauschild
 */
public class FlexibleSearch extends Base<FlexibleSearchQuery, QueryResult> {

  private static final String PATH = "/flexsearch";

  public FlexibleSearch(final Configuration configuration) {
    super(configuration, QueryResult.class);
  }

  /**
   * Perform given query
   *
   * @param query query
   * @return result
   */
  public QueryResult query(final FlexibleSearchQuery query) {
    return execute(query, PATH);
  }
}
