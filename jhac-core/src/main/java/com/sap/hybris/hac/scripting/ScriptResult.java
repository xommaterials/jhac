package com.sap.hybris.hac.scripting;

import lombok.Getter;
import lombok.ToString;

/**
 * Script execution result.
 *
 * <p>At first you should check for {@link #hasError()}. Either you find further error details in
 * {@link #getStacktraceText()} or {@link #getOutputText()} and {@link #getExecutionResult()} are
 * probably filled. If {@link #getExecutionResult()} is filled you can used <code>asXXX()</code> to
 * receive execution result parsed into respective type.
 *
 * @author Klaus Hauschild
 */
@Getter
@ToString
public class ScriptResult {

  private String executionResult;
  private String outputText;
  private String stacktraceText;

  /**
   * Determines if script execution has error.
   *
   * @return <code>true</code> if script execution failed, see {@link #getStacktraceText()} for
   *     further details<br>
   *     <code>false</code> if script execution was successful
   */
  public boolean hasError() {
    return stacktraceText.length() > 0;
  }

  public String asString() {
    return executionResult;
  }

  public boolean asBoolean() {
    return Boolean.parseBoolean(executionResult);
  }

  public int asInt() {
    return Integer.parseInt(executionResult);
  }

  public long asLong() {
    return Long.parseLong(executionResult);
  }

  public float asFloat() {
    return Float.parseFloat(executionResult);
  }

  public double asDouble() {
    return Double.parseDouble(executionResult);
  }

  public byte asByte() {
    return Byte.parseByte(executionResult);
  }
}
