package com.sap.hybris.hac.scripting;

import com.sap.hybris.hac.Base;
import com.sap.hybris.hac.Configuration;
import org.slf4j.LoggerFactory;

public class Scripting extends Base<Script, ScriptResult> {

  private static final String PATH = "/scripting";

  public Scripting(final Configuration configuration) {
    super(LoggerFactory.getLogger(Scripting.class), configuration, ScriptResult.class);
  }

  public ScriptResult execute(final Script script) {
    return execute(script, PATH);
  }
}
