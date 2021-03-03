package com.sap.hybris.hac.exception;

public class ConnectionException extends RuntimeException {

  public ConnectionException(final String message, final Exception cause) {
    super(message, cause);
  }
}
