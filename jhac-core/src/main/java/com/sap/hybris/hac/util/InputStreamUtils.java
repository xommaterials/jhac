package com.sap.hybris.hac.util;

import static java.util.stream.Collectors.joining;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Utilities for {@link InputStream}.
 *
 * @author Klaus Hauschild
 */
public enum InputStreamUtils {
  ;

  public static String readLines(final InputStream inputStream, final String name) {
    try (final BufferedReader buffer = new BufferedReader(new InputStreamReader(inputStream))) {
      return buffer.lines().collect(joining("\n"));
    } catch (final IOException exception) {
      throw new IllegalArgumentException(String.format("unable to read %s", name), exception);
    }
  }
}
