package com.sap.hybris.hac.impex;

import static com.sap.hybris.hac.HybrisAdministrationConsole.hac;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import org.junit.Test;

public class ImportExportIT {

  @Test
  public void importData() {
    final ImpexResult result = //
        hac() //
            .impex() //
            .importData( //
                Impex.builder() //
                    .scriptContent(
                        "INSERT_UPDATE user; uid[unique = true]\n" //
                            + "; admin") //
                    .buildImport());
    assertThat(result.hasError(), is(false));
  }

  @Test
  public void exportData() {
    final ImpexResult result = //
        hac() //
            .impex() //
            .exportData( //
                Impex.builder() //
                    .scriptContent("INSERT_UPDATE user; uid[unique = true]") //
                    .buildExport());
    assertThat(result.hasError(), is(false));
    assertThat(result.getExportResources().size(), is(1));
  }

  @Test
  public void importError() {
    final ImpexResult result = //
        hac() //
            .impex() //
            .importData( //
                Impex.builder() //
                    .scriptContent("invalid") //
                    .buildImport());
    assertThat(result.hasError(), is(true));
  }
}
