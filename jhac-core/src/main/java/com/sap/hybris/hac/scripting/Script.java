package com.sap.hybris.hac.scripting;

import com.sap.hybris.hac.Request;
import lombok.Builder;
import lombok.Builder.Default;
import lombok.Getter;
import lombok.ToString;

import java.util.Objects;

@Builder
@Getter
@ToString
public class Script implements Request {

  private String script;
  @Default private ScriptType scriptType = ScriptType.groovy;
  @Default private boolean commit = false;

  @Override
  public void validate() {
    Objects.requireNonNull(script, "script must not be null");
  }
}
