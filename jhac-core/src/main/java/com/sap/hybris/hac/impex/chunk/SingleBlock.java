package com.sap.hybris.hac.impex.chunk;

import com.google.common.collect.Lists;
import com.google.common.collect.Streams;
import com.sap.hybris.hac.impex.Impex;
import com.sap.hybris.hac.util.Utils;
import lombok.AllArgsConstructor;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * {@link Strategy Chunking strategy} capable of handling an {@link Impex} containing a single block
 * (head + data).
 *
 * <ul>
 *   <li>first line starting with <code>;</code> determines first data line
 *   <li>everything above is declared head
 *   <li>everything below is declared data
 *   <li>chunked Impex will always include head
 *   <li>chunked Impex will have partly data based on chunk size
 * </ul>
 *
 * @author Klaus Hauschild
 */
@AllArgsConstructor
public class SingleBlock implements Strategy {

  private final int chunkSize;

  @Override
  public Impex[] apply(final Impex impex) {
    final List<String> lines =
        Arrays.stream(impex.getScriptContent().split("\n")) //
            .map(String::trim) //
            .collect(Collectors.toList());

    // find first data line
    int firstDataLine = -1;
    for (int i = 0; i < lines.size(); i++) {
      if (lines.get(i).startsWith(";")) {
        firstDataLine = i;
        break;
      }
    }
    if (firstDataLine == -1) {
      throw new IllegalArgumentException(
          String.format("Unable to determine head in Impex block [%s]", impex.getScriptContent()));
    }

    // extract header
    final List<String> head =
        lines.stream() //
            .limit(firstDataLine) //
            .collect(Collectors.toList());

    // extract data
    final List<String> data =
        lines.stream() //
            .skip(firstDataLine) //
            .collect(Collectors.toList());

    // chunk data
    final List<List<String>> chunkedData = Lists.partition(data, chunkSize);
    return chunkedData.stream() //
        .map(
            dataChunk -> {
              final String chunkedLines =
                  Utils.joinLines(
                      Streams.concat( //
                          head.stream(), //
                          dataChunk.stream() //
                          ));
              return Impex.builder() //
                  .scriptContent(chunkedLines) //
                  .buildImport();
            }) //
        .toArray(Impex[]::new);
  }
}
