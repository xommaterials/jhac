package com.sap.hybris.hac.scripting;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class ScriptResult {

  private String executionResult;
  private String outputText;
  private String stacktraceText;
}
