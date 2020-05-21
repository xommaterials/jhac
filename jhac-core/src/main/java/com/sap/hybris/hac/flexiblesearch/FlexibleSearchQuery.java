package com.sap.hybris.hac.flexiblesearch;

import com.sap.hybris.hac.Request;
import java.util.Locale;
import lombok.Builder;
import lombok.Builder.Default;
import lombok.Getter;
import lombok.ToString;

/**
 * Flexible search / SQL query
 *
 * @author Klaus Hauschild
 */
@Builder
@Getter
@ToString
public class FlexibleSearchQuery implements Request {

  private String flexibleSearchQuery;
  private String sqlQuery;
  @Default private int maxCount = 200;
  @Default private String user = "admin";
  @Default private Locale locale = Locale.ENGLISH;
  private boolean commit;

  @Override
  public void validate() {
    if (flexibleSearchQuery == null && sqlQuery == null) {
      throw new IllegalArgumentException("either flexibleSearchQuery or sqlQuery must not be null");
    }
    if (flexibleSearchQuery != null && sqlQuery != null) {
      throw new IllegalArgumentException("either flexibleSearchQuery or sqlQuery must be used");
    }
    if (maxCount <= 0) {
      throw new IllegalArgumentException("maxCount must be positive");
    }
    if (user == null) {
      throw new IllegalArgumentException("user must not be null");
    }
    if (locale == null) {
      throw new IllegalArgumentException("locale must not be null");
    }
  }
}
