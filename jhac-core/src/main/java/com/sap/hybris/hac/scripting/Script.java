package com.sap.hybris.hac.scripting;

import static java.util.stream.Collectors.joining;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
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

  static class ScriptBuilder {

    ScriptBuilder script(final String script) {
      this.script = script;
      return this;
    }

    ScriptBuilder script(final InputStream script) {
      try (final BufferedReader buffer = new BufferedReader(new InputStreamReader(script))) {
        this.script = buffer.lines().collect(joining("\n"));
        return this;
      } catch (final IOException exception) {
        throw new IllegalArgumentException("unable to read script", exception);
      }
    }

    Script build() {
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
