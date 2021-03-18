package com.sap.hybris.hac.impex;

import com.sap.hybris.hac.impex.ImpexError.Type;
import org.junit.Test;

import java.util.List;

import static com.sap.hybris.hac.HybrisAdministrationConsole.hac;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;

public class ImportExportErrorIT {

  @Test
  public void unknownTypeInHeader() {
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
    final List<ImpexError> parsedErrors = result.parseErrors();
    assertThat(parsedErrors, hasSize(1));
    assertThat(parsedErrors.get(0).getType(), is(Type.HEADER));
  }

  @Test
  public void unknownAttributeInHeader() {
    final ImpexResult result = //
        hac() //
            .impex() //
            .importData( //
                Impex.builder() //
                    .scriptContent(
                        "UPDATE Product; foobar\n" //
                            + "; product") //
                    .buildImport());
    assertThat(result.hasError(), is(true));
    final List<ImpexError> parsedErrors = result.parseErrors();
    assertThat(parsedErrors, hasSize(1));
    assertThat(parsedErrors.get(0).getType(), is(Type.HEADER));
  }

  @Test
  public void unresolvableReferenceInData() {
    final ImpexResult result = //
        hac() //
            .impex() //
            .importData( //
                Impex.builder() //
                    .scriptContent(
                        "UPDATE Product; code[unique = true]\n" //
                            + "; __this_product_defenitively_does_not_exist__") //
                    .buildImport());
    assertThat(result.hasError(), is(true));
    final List<ImpexError> parsedErrors = result.parseErrors();
    assertThat(parsedErrors, hasSize(1));
    assertThat(parsedErrors.get(0).getType(), is(Type.DATA));
  }
}
