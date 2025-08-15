package com.forge;

import com.forge.cli.AnalyzeCommand;
import com.forge.cli.SuggestCommand;
import com.forge.cli.EstimateCommand;
import picocli.CommandLine;

@CommandLine.Command(
    name = "forge",
    mixinStandardHelpOptions = true,
    version = "1.0.0",
    description = "Analyze codebases for inefficiencies and suggest greener alternatives",
    subcommands = {
        AnalyzeCommand.class,
        SuggestCommand.class,
        EstimateCommand.class
    }
)
public class ForgeMain {
    public static void main(String[] args) {
        int exitCode = new CommandLine(new ForgeMain()).execute(args);
        System.exit(exitCode);
    }
}
