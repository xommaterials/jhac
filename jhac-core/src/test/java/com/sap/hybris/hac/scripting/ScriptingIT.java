package com.sap.hybris.hac.scripting;

import org.junit.Test;

import static com.sap.hybris.hac.HybrisAdministrationConsole.hac;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class ScriptingIT {

  @Test
  public void groovyRocks() {
    final ScriptResult result =
        hac() //
            .scripting() //
            .execute(
                Script.builder() //
                    .script(
                        "spring.beanDefinitionNames.each {\n" //
                            + "    println it\n" //
                            + "}\n" //
                            + "return \"Groovy Rocks!\"") //
                    .build());
    assertThat(result.hasError(), is(false));
  }

  @Test
  public void executionError() {
    final ScriptResult result =
        hac() //
            .scripting() //
            .execute( //
                Script.builder() //
                    .script("foobar") //
                    .build());
    assertThat(result.hasError(), is(true));
  }

  @Test
  public void asInt() {
    final ScriptResult result =
        hac() //
            .scripting() //
            .execute( //
                Script.builder() //
                    .script("2 + 2") //
                    .build());
    assertThat(result.hasError(), is(false));
    assertThat(result.asInt(), is(4));
  }
}
