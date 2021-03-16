package com.sap.hybris.hac.impex;

import com.sap.hybris.hac.Result;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

/**
 * Import / export result.
 *
 * @author Klaus Hauschild
 */
@AllArgsConstructor
@Getter
@ToString
public class ImpexResult implements Result {

  private List<String> errors;
  private List<byte[]> exportResources;

  @Override
  public boolean hasError() {
    return !errors.isEmpty();
  }

  public List<ParseError> parseErrors() {
    throw new UnsupportedOperationException("not implemented yet");
  }

  @AllArgsConstructor
  @Getter
  @ToString
  static class ParseError {

    private final Type type;

    enum Type {
      HEADER,

      DATA,

      UNKNOWN,
    }
  }
}
