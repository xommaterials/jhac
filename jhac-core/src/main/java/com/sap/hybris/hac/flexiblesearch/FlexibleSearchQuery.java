package com.sap.hybris.hac.flexiblesearch;

import static com.sap.hybris.hac.util.InputStreamUtils.readLines;

import java.io.InputStream;
import java.util.Locale;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

/**
 * Flexible search / SQL query
 *
 * @author Klaus Hauschild
 */
@Builder(builderClassName = "FlexibleSearchQueryBuilder")
@Getter
@ToString
public class FlexibleSearchQuery {

  private String flexibleSearchQuery;
  private String sqlQuery;
  private int maxCount;
  private String user;
  private Locale locale;
  private boolean commit;

  static class FlexibleSearchQueryBuilder {

    FlexibleSearchQueryBuilder flexibleSearchQuery(final String flexibleSearchQuery) {
      this.flexibleSearchQuery = flexibleSearchQuery;
      return this;
    }

    FlexibleSearchQueryBuilder flexibleSearchQuery(final InputStream flexibleSearchQuery) {
      this.flexibleSearchQuery = readLines(flexibleSearchQuery, "flexible search query");
      return this;
    }

    FlexibleSearchQueryBuilder sqlQuery(final String sqlQuery) {
      this.sqlQuery = sqlQuery;
      return this;
    }

    FlexibleSearchQueryBuilder sqlQuery(final InputStream sqlQuery) {
      this.sqlQuery = readLines(sqlQuery, "sql query");
      return this;
    }

    FlexibleSearchQuery build() {
      // validation
      if (flexibleSearchQuery == null && sqlQuery == null) {
        throw new IllegalArgumentException(
            "either flexibleSearchQuery or sqlQuery must not be null");
      }
      if (flexibleSearchQuery != null && sqlQuery != null) {
        throw new IllegalArgumentException("either flexibleSearchQuery or sqlQuery must be used");
      }

      // default values
      if (maxCount <= 0) {
        maxCount = 200;
      }
      if (user == null) {
        user = "admin";
      }
      if (locale == null) {
        locale = Locale.ENGLISH;
      }

      // build
      return new FlexibleSearchQuery(flexibleSearchQuery, sqlQuery, maxCount, user, locale, commit);
    }
  }
}
