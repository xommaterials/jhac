package com.sap.hybris.hac.cli;

import com.sap.hybris.hac.Configuration;
import com.sap.hybris.hac.Configuration.ConfigurationBuilder;
import com.sap.hybris.hac.scripting.Script;
import com.sap.hybris.hac.scripting.ScriptResult;
import com.sap.hybris.hac.scripting.ScriptType;
import org.apache.commons.io.FilenameUtils;
import picocli.CommandLine;
import picocli.CommandLine.ArgGroup;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.Callable;

import static com.sap.hybris.hac.HybrisAdministrationConsole.hac;

@Command(name = "jhac-cli", version = "jhac-cli 1.0")
public class HacCli implements Callable<Integer> {

  private static CommandLine commandLine;

  @Option(
      names = {"-v", "--version"},
      versionHelp = true,
      description = "display version info")
  private boolean versionInfoRequested;

  @Option(
      names = {"-h", "--help"},
      usageHelp = true,
      description = "display this help message")
  private boolean usageHelpRequested;

  @ArgGroup private ConfigurationGroup configuration;

  @Option(names = "--commit", description = "commit changes")
  private boolean commit;

  @Option(names = "--debug")
  private boolean debug;

  @Parameters(
      description =
          "file to process\n  .groovy -> scripting\n  .fxs -> flexible search\n  .sql -> SQL\n  .import -> Impex import\n  .export -> Impex export")
  private File file;

  public static void main(final String[] args) {
    commandLine = new CommandLine(new HacCli());
    commandLine.parseArgs(args);
    if (commandLine.isUsageHelpRequested()) {
      commandLine.usage(System.out);
      return;
    } else if (commandLine.isVersionHelpRequested()) {
      commandLine.printVersionHelp(System.out);
      return;
    }
    final int exitCode = commandLine.execute(args);
    System.exit(exitCode);
  }

  @Override
  public Integer call() throws Exception {
    try {
      final Configuration configuration = buildConfiguration();
      if (!file.exists() || !file.canRead()) {
        throw new FileNotFoundException("file not found or readable: " + file);
      }
      final String extension = FilenameUtils.getExtension(file.getName());
      switch (extension.toLowerCase()) {
        case "groovy":
          groovy(configuration);
          break;
        case "fxs":
          flexibleSearch(configuration);
          break;
        case "sql":
          sql(configuration);
          break;
        case "import":
          importData(configuration);
          break;
        case "export":
          exportData(configuration);
          break;
      }
      return 0;
    } catch (final Exception exception) {
      System.err.println(exception.getMessage());
      return 1;
    }
  }

  private void groovy(final Configuration configuration) throws IOException {
    try (final InputStream inputStream = new BufferedInputStream(new FileInputStream(file))) {
      final ScriptResult scriptResult =
          hac(configuration) //
              .scripting() //
              .execute( //
                  Script.builder() //
                      .scriptType(ScriptType.groovy) //
                      .commit(commit)
                      .script(inputStream)
                      .build());
      if (scriptResult.hasError()) {
        System.err.println(scriptResult.getStacktraceText());
      } else {
        if (debug) {
          System.err.println(scriptResult.getOutputText());
        }
        System.out.println(scriptResult.getExecutionResult());
      }
    }
  }

  private void flexibleSearch(final Configuration configuration) {}

  private void sql(final Configuration configuration) {}

  private void importData(final Configuration configuration) {}

  private void exportData(final Configuration configuration) {}

  private Configuration buildConfiguration() {
    final ConfigurationBuilder builder = Configuration.builder();
    if (configuration != null && configuration.configurationFile != null) {
      try (final InputStream inputStream =
          new BufferedInputStream(new FileInputStream(configuration.configurationFile))) {
        builder.from(inputStream);
      } catch (final IOException exception) {
        throw new IllegalArgumentException(
            "Unable to load configuration file: " + configuration.configurationFile);
      }
    } else if (configuration != null && configuration.configurationParams != null) {
      builder
          .endpoint(configuration.configurationParams.endpoint) //
          .username(configuration.configurationParams.username) //
          .password(configuration.configurationParams.password);
    }
    return builder.build();
  }

  static class ConfigurationGroup {

    @Option(
        names = {"-C", "-configuration"},
        required = true,
        description = "configuration file")
    File configurationFile;

    @ArgGroup(exclusive = false)
    private ConfigurationParams configurationParams;

    static class ConfigurationParams {
      @Option(
          names = {"-e", "-endpoint"},
          required = true)
      String endpoint;

      @Option(
          names = {"-u", "-username"},
          required = true)
      String username;

      @Option(
          names = {"-p", "-password"},
          required = true)
      String password;
    }
  }
}
