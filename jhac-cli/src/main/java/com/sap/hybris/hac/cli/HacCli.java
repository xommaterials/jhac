package com.sap.hybris.hac.cli;

import java.util.concurrent.Callable;
import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

@Command(name = "jhac-cli", version = "jhac-cli 1.0.0")
public class HacCli implements Callable<Integer> {

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

  public static void main(final String[] args) {
    final CommandLine commandLine = new CommandLine(new HacCli());
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
    return 0;
  }
}
