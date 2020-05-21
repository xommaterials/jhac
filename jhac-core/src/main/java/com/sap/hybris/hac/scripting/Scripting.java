package com.sap.hybris.hac.scripting;

import com.sap.hybris.hac.Base;
import com.sap.hybris.hac.Configuration;

/**
 * Scripting endpoint.
 *
 * @author Klaus Hauschild
 */
public class Scripting extends Base<Script, ScriptResult> {

  private static final String PATH = "/scripting";

  public Scripting(final Configuration configuration) {
    super(configuration, ScriptResult.class);
  }

  /**
   * Execute given script.
   *
   * @param script script to execute
   * @return execution result
   */
  public ScriptResult execute(final Script script) {
    return execute(script, PATH);
  }
}
