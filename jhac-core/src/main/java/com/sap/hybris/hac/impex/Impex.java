package com.sap.hybris.hac.impex;

import com.sap.hybris.hac.impex.chunk.SingleBlock;
import com.sap.hybris.hac.impex.chunk.Strategy;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

import static com.sap.hybris.hac.util.Utils.readLines;

/**
 * Impex.
 *
 * @author Klaus Hauschild
 */
@Builder(builderClassName = "ImpexBuilder", buildMethodName = "__do_not_use_me__")
@Getter
@ToString
public class Impex {

  public static final List<String> KEY_WORDS =
      Arrays.asList("INSERT", "UPDATE", "INSERT_UPDATE", "REMOVE");

  public static boolean startsWithKeyword(final String line) {
    final String firstWord = line.split(" ", 2)[0];
    return Impex.KEY_WORDS.contains(firstWord);
  }

  private static final int DEFAULT_CHUNK_SIZE = 1000;

  private String scriptContent;
  private Validation validationEnum;
  private Integer maxThreads;
  private String encoding;
  private Boolean _legacyMode;
  private Boolean _enableCodeExecution;
  private Boolean _distributedMode;
  private Boolean _sldEnabled;

  public Impex[] chunked() {
    return chunked(DEFAULT_CHUNK_SIZE);
  }

  public Impex[] chunked(final int chunkSize) {
    return chunked(new SingleBlock(chunkSize));
  }

  /**
   * Will split the current Impex into multiple chunks using the given {@link
   * com.sap.hybris.hac.impex.chunk.Strategy Strategy}.
   *
   * @return chunked Impexes
   */
  public Impex[] chunked(final Strategy strategy) {
    return strategy.apply(this);
  }

  public static class ImpexBuilder {

    public ImpexBuilder scriptContent(final String scriptContent) {
      this.scriptContent = scriptContent;
      return this;
    }

    public ImpexBuilder scriptContent(final InputStream scriptContent) {
      this.scriptContent = readLines(scriptContent, "script content");
      return this;
    }

    public Impex buildImport() {
      return build(Validation.IMPORT_STRICT);
    }

    public Impex build(final Validation validationDefault) {
      // validation
      if (scriptContent == null) {
        throw new IllegalArgumentException("script content must not be null");
      }

      // default values
      if (validationEnum == null) {
        validationEnum = validationDefault;
      }
      if (maxThreads == null) {
        maxThreads = 24;
      }
      if (encoding == null) {
        encoding = "UTF-8";
      }
      if (_legacyMode == null) {
        _legacyMode = true;
      }
      if (_enableCodeExecution == null) {
        _enableCodeExecution = true;
      }
      if (_distributedMode == null) {
        _distributedMode = true;
      }
      if (_sldEnabled == null) {
        _sldEnabled = true;
      }

      // build
      return new Impex(
          scriptContent,
          validationEnum,
          maxThreads,
          encoding,
          _legacyMode,
          _enableCodeExecution,
          _distributedMode,
          _sldEnabled);
    }

    public Impex buildExport() {
      return build(Validation.EXPORT_ONLY);
    }
  }
}
