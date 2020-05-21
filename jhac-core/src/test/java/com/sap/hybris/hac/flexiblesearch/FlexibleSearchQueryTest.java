package com.sap.hybris.hac.flexiblesearch;

import org.junit.Test;

public class FlexibleSearchQueryTest {

  @Test(expected = IllegalArgumentException.class)
  public void nullFlexibleSearchSQLQuery() {
    FlexibleSearchQuery.builder().flexibleSearchQuery(null).sqlQuery(null).build().validate();
  }

  @Test(expected = IllegalArgumentException.class)
  public void noneNullFlexibleSearchSQLQuery() {
    FlexibleSearchQuery.builder().flexibleSearchQuery("").sqlQuery("").build().validate();
  }

  @Test(expected = IllegalArgumentException.class)
  public void negativeMaxCount() {
    FlexibleSearchQuery.builder().flexibleSearchQuery("").maxCount(-1).build().validate();
  }

  @Test(expected = IllegalArgumentException.class)
  public void nullUser() {
    FlexibleSearchQuery.builder().flexibleSearchQuery("").user(null).build().validate();
  }

  @Test(expected = IllegalArgumentException.class)
  public void nullLocale() {
    FlexibleSearchQuery.builder().flexibleSearchQuery("").locale(null).build().validate();
  }
}
