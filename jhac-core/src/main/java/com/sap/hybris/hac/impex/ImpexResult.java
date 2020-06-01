package com.sap.hybris.hac.impex;

import java.util.List;

import com.sap.hybris.hac.Result;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

/**
 * Import / export result.
 *
 * @author Klaus Hauschild
 */
@AllArgsConstructor
@Getter
@ToString
public class ImpexResult implements Result {

  private String error;
  private List<byte[]> exportResources;

  @Override
  public boolean hasError() {
    return !error.isEmpty();
  }
}
