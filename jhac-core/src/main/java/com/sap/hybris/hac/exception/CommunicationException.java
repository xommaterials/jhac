package com.sap.hybris.hac.exception;

import lombok.Getter;

/** @author Klaus Hauschild */
public class CommunicationException extends RuntimeException {

  @Getter private final Object request;

  public <REQUEST> CommunicationException(
      final String message, final REQUEST request, final Throwable cause) {
    super(message, cause);
    this.request = request;
  }
}
