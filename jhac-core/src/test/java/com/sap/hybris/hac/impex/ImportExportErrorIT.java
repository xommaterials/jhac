package com.sap.hybris.hac.impex;

import org.junit.Test;

import static com.sap.hybris.hac.HybrisAdministrationConsole.hac;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class ImportExportErrorIT {

  @Test
  public void unknownType() {
    final ImpexResult result = //
        hac() //
            .impex() //
            .importData( //
                Impex.builder() //
                    .scriptContent(
                        "INSERT_UPDATE foobar; uid[unique = true]\n" //
                            + "; admin") //
                    .buildImport());
    assertThat(result.hasError(), is(true));
  }
}
