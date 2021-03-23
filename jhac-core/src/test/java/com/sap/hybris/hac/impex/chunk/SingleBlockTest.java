package com.sap.hybris.hac.impex.chunk;

import com.sap.hybris.hac.impex.Impex;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.collection.IsArrayWithSize.arrayWithSize;

public class SingleBlockTest {

  @Test
  public void chunk() throws IOException {
    try (final InputStream inputStream =
        SingleBlockTest.class.getResourceAsStream("single-block.impex")) {
      final Impex impex = Impex.builder().scriptContent(inputStream).buildImport();
      final Impex[] chunked = impex.chunked(new SingleBlock(1));
      assertThat(chunked, arrayWithSize(3));
    }
  }
}
