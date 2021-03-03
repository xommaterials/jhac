package com.sap.hybris.hac.exception;

import lombok.Getter;
import org.springframework.web.client.RestClientException;

public class CommunicationException extends RuntimeException {

  @Getter private final Object request;

  public <REQUEST> CommunicationException(
      final String message, final REQUEST request, final Exception cause) {
    super(message, cause);
    this.request = request;
  }
}
