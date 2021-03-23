package com.sap.hybris.hac.flexiblesearch;

import org.junit.Test;

import java.util.Locale;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;

public class FlexibleSearchQueryTest {

  @Test(expected = IllegalArgumentException.class)
  public void flexibleSearchAndSqlAreNull() {
    FlexibleSearchQuery.builder()
        .flexibleSearchQuery((String) null)
        .sqlQuery((String) null)
        .build();
  }

  @Test(expected = IllegalArgumentException.class)
  public void flexibleSearchAndSqlAreFilled() {
    FlexibleSearchQuery.builder().flexibleSearchQuery("flexibleSearch").sqlQuery("sql").build();
  }

  @Test
  public void defaultValues() {
    final FlexibleSearchQuery flexibleSearch =
        FlexibleSearchQuery.builder().flexibleSearchQuery("flexibleSearch").build();
    assertThat(flexibleSearch.getFlexibleSearchQuery(), is("flexibleSearch"));
    assertThat(flexibleSearch.getSqlQuery(), is(nullValue()));
    assertThat(flexibleSearch.getMaxCount(), is(200));
    assertThat(flexibleSearch.getUser(), is("admin"));
    assertThat(flexibleSearch.getLocale(), is(Locale.ENGLISH));
    assertThat(flexibleSearch.isCommit(), is(false));
  }
}
