package com.sap.hybris.hac.scripting;

import com.sap.hybris.hac.Request;
import lombok.Builder;
import lombok.Builder.Default;
import lombok.Getter;
import lombok.ToString;

/**
 * Script. By default script type is set to <code>groovy</code> and commit mode is deactivated.
 *
 * @author Klaus Hauschild
 */
@Builder
@Getter
@ToString
public class Script implements Request {

  private String script;
  @Default private ScriptType scriptType = ScriptType.groovy;
  private boolean commit;

  @Override
  public void validate() {
    if (script == null) {
      throw new IllegalArgumentException("script must not be null");
    }
    if (scriptType == null) {
      throw new IllegalArgumentException("scriptType must not be null");
    }
  }
}
