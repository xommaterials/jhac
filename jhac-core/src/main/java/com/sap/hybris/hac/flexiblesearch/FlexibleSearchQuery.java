package com.sap.hybris.hac.flexiblesearch;

import static java.util.stream.Collectors.joining;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
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
      try (final BufferedReader buffer =
          new BufferedReader(new InputStreamReader(flexibleSearchQuery))) {
        this.flexibleSearchQuery = buffer.lines().collect(joining("\n"));
        return this;
      } catch (final IOException exception) {
        throw new IllegalArgumentException("unable to read flexible search suery", exception);
      }
    }

    FlexibleSearchQueryBuilder sqlQuery(final String sqlQuery) {
      this.sqlQuery = sqlQuery;
      return this;
    }

    FlexibleSearchQueryBuilder sqlQuery(final InputStream sqlQuery) {
      try (final BufferedReader buffer = new BufferedReader(new InputStreamReader(sqlQuery))) {
        this.sqlQuery = buffer.lines().collect(joining("\n"));
        return this;
      } catch (final IOException exception) {
        throw new IllegalArgumentException("unable to read sql query", exception);
      }
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
