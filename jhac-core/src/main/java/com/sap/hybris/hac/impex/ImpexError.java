package com.sap.hybris.hac.impex;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@RequiredArgsConstructor
@Getter
@ToString
public class ImpexError {

  private final Type type;
  @Setter private String message;

  enum Type {
    HEADER,

    DATA,

    UNKNOWN,
  }
}
