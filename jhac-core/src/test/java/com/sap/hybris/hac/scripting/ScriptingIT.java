package com.sap.hybris.hac.scripting;

import static com.sap.hybris.hac.HybrisAdministrationConsole.hac;

public class ScriptingIT {

  public static void main(final String[] args) {
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
  }
}
