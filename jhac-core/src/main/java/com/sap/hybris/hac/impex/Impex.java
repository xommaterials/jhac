package com.sap.hybris.hac.impex;

import static com.sap.hybris.hac.util.InputStreamUtils.readLines;

import java.io.InputStream;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

/**
 * Impex.
 *
 * @author Klaus Hauschild
 */
@Builder(builderClassName = "ImpexBuilder", buildMethodName = "__do_not_use_me__")
@Getter
@ToString
public class Impex {

  private String scriptContent;
  private Validation validationEnum;
  private Integer maxThreads;
  private String encoding;
  private Boolean _legacyMode;
  private Boolean _enableCodeExecution;
  private Boolean _distributedMode;
  private Boolean _sldEnabled;

  static class ImpexBuilder {

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
