package com.sap.hybris.hac.flexiblesearch;

import org.junit.Test;

import java.util.List;
import java.util.Map;

import static com.sap.hybris.hac.HybrisAdministrationConsole.hac;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;

public class FlexibleSearchIT {

  @Test
  public void selectAllProducts() {
    final QueryResult result = //
        hac() //
            .flexibleSearch() //
            .query( //
                FlexibleSearchQuery.builder() //
                    .flexibleSearchQuery("SELECT * FROM { Product }") //
                    .maxCount(1) //
                    .build());
    assertThat(result.hasError(), is(false));
    assertThat(result.getResultCount(), is(1));
  }

  @Test
  public void asMap() {
    final QueryResult result = //
        hac() //
            .flexibleSearch() //
            .query( //
                FlexibleSearchQuery.builder() //
                    .flexibleSearchQuery("SELECT * FROM { Product }") //
                    .maxCount(1) //
                    .build());
    assertThat(result.hasError(), is(false));

    final List<Map<String, String>> resultMap = result.asMap();
    assertThat(resultMap, hasSize(1));
    // PK column is typically upper-case
    assertThat(resultMap.get(0).containsKey("PK"), is(false));
    assertThat(resultMap.get(0).containsKey("pk"), is(true));
  }

  @Test
  public void sqlQuery() {
    final QueryResult result = //
        hac() //
            .flexibleSearch() //
            .query( //
                FlexibleSearchQuery.builder() //
                    .sqlQuery("SELECT * FROM products") //
                    .maxCount(1) //
                    .build());
    assertThat(result.hasError(), is(false));
    assertThat(result.getResultCount(), is(1));
  }

  @Test
  public void queryException() {
    final QueryResult result = //
        hac() //
            .flexibleSearch() //
            .query( //
                FlexibleSearchQuery.builder() //
                    .flexibleSearchQuery("invalid") //
                    .build());
    assertThat(result.hasError(), is(true));
  }

  @Test
  public void countQuery() {
    final QueryResult result = //
        hac() //
            .flexibleSearch() //
            .query( //
                FlexibleSearchQuery.builder() //
                    .flexibleSearchQuery("SELECT COUNT(*) FROM { Product }") //
                    .build());
    assertThat(result.hasError(), is(false));
    assertThat(result.count() > 0, is(true));
  }
}
