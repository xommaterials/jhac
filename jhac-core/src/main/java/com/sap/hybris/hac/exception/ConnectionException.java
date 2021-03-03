package com.sap.hybris.hac.exception;

import org.springframework.web.client.RestClientException;

public class ConnectionException extends RuntimeException {

  public ConnectionException(final String message, final RestClientException cause) {
    super(message, cause);
  }
}
