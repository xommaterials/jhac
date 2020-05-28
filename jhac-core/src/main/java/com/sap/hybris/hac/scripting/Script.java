package com.sap.hybris.hac.scripting;

import static com.sap.hybris.hac.util.InputStreamUtils.readLines;

import java.io.InputStream;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

/**
 * Script. By default script type is set to <code>groovy</code> and commit mode is deactivated.
 *
 * @author Klaus Hauschild
 */
@Builder(builderClassName = "ScriptBuilder")
@Getter
@ToString
public class Script {

  private String script;
  private ScriptType scriptType;
  private boolean commit;

  public static class ScriptBuilder {

    public ScriptBuilder script(final String script) {
      this.script = script;
      return this;
    }

    public ScriptBuilder script(final InputStream script) {
      this.script = readLines(script, "script");
      return this;
    }

    public Script build() {
      // validation
      if (script == null) {
        throw new IllegalArgumentException("script must not be null");
      }

      // default values
      if (scriptType == null) {
        scriptType = ScriptType.groovy;
      }

      // build
      return new Script(script, scriptType, commit);
    }
  }
}
