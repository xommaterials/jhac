package com.sap.hybris.hac.impex;

import com.sap.hybris.hac.Result;
import com.sap.hybris.hac.impex.ImpexError.Type;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.Iterator;
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

  private final List<String> errors;
  private final List<byte[]> exportResources;

  @Override
  public boolean hasError() {
    return !errors.isEmpty();
  }

  public List<ImpexError> parseErrors() {
    final List<ImpexError> parsedErrors = new ArrayList<>();

    ImpexError impexError;
    final Iterator<String> iterator = errors.iterator();
    while (iterator.hasNext()) {
      String error = iterator.next();
      final String firstWord = error.split(" ", 2)[0];
      if (!Impex.KEY_WORDS.contains(firstWord)) {
        continue;
      }
      impexError = new ImpexError(error.contains("#") ? Type.HEADER : Type.DATA);
      switch (impexError.getType()) {
        case HEADER:
          impexError.setMessage(error.split("#")[1].trim());
          break;
        case DATA:
          impexError.setMessage(iterator.next().replaceFirst(",,,,", "").trim());
          break;
        default:
          impexError.setMessage(error);
          break;
      }
      parsedErrors.add(impexError);
    }

    return parsedErrors;
  }
}
