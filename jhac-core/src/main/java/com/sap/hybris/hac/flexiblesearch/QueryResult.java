package com.sap.hybris.hac.flexiblesearch;

import lombok.Getter;
import lombok.ToString;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Flexible search / SQL query result.
 *
 * @author Klaus Hauschild
 */
@Getter
@ToString
public class QueryResult {

  private String catalogVersionsAsString;
  private Throwable exception;
  private String exceptionStackTrace;
  private long executionTime;
  private List<String> headers;
  private String parametersAsString;
  private String query;
  private boolean rawExecution;
  //  private int resultCount;
  private List<List<String>> resultList;

  /**
   * Returns the number of results. If query was not successful <code>0</code> will be returned.
   *
   * @return number of results
   */
  public int getResultCount() {
    // returned resultCount is not populated, derive its value from resultList
    if (hasError()) {
      return 0;
    }
    return resultList.size();
  }

  public boolean hasError() {
    return exception != null;
  }

  /**
   * Convenience method to receive every result entry as association list (header -> value).
   *
   * @return result as association lists
   */
  public List<Map<String, String>> asMap() {
    return resultList.stream()
        .map(
            line -> {
              final Map<String, String> entry = new HashMap<>();
              for (int i = 0; i < headers.size(); i++) {
                entry.put(headers.get(i), line.get(i));
              }
              return entry;
            })
        .collect(Collectors.toList());
  }

  /**
   * For any simple <code>SELECT COUNT</code> query its result will be returned as <code>int</code>.
   *
   * @return result of <code>SELECT COUNT</code> query
   */
  public int count() {
    if (!query.toLowerCase().contains("count")) {
      throw new IllegalStateException("no SELECT COUNT query");
    }
    if (resultList.size() != 1) {
      throw new IllegalStateException("insufficient result list");
    }
    if (resultList.get(0).size() != 1) {
      throw new IllegalStateException("insufficient result list");
    }
    return Integer.parseInt(resultList.get(0).get(0));
  }
}
