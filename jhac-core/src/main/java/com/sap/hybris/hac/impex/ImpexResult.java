package com.sap.hybris.hac.impex;

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
public class ImpexResult {

  private String error;
  private List<byte[]> exportResources;

  public boolean hasError() {
    return !error.isEmpty();
  }
}
