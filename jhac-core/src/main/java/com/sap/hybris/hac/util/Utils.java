package com.sap.hybris.hac.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.stream.Stream;

import static java.util.stream.Collectors.joining;

/**
 * Utilities.
 *
 * @author Klaus Hauschild
 */
public enum Utils {
  ;

  public static String joinLines(final Stream<String> lines) {
    return lines.collect(joining("\n"));
  }

  public static String readLines(final InputStream inputStream, final String name) {
    try (final BufferedReader buffer = new BufferedReader(new InputStreamReader(inputStream))) {
      return joinLines(buffer.lines());
    } catch (final IOException exception) {
      throw new IllegalArgumentException(String.format("unable to read %s", name), exception);
    }
  }
}
