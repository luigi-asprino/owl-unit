package it.cnr.istc.stlab.owlunit;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import it.cnr.istc.stlab.owlunit.workers.TestSuiteExecutor;

public class OWLUnit {

	private static final String TEST_SUITE = "t";
	private static final Logger logger = LoggerFactory.getLogger(OWLUnit.class);

	public static void main(String[] args) {

		Options options = new Options();

		options.addOption(Option.builder(TEST_SUITE).argName("URI").hasArg().required(true)
				.desc("The URI of the test suite to execute.").longOpt("test-suite").build());

		CommandLine commandLine = null;

		CommandLineParser cmdLineParser = new DefaultParser();
		try {
			commandLine = cmdLineParser.parse(options, args);

			if (commandLine.hasOption(TEST_SUITE)) {
				logger.info("Test suite URI {}", commandLine.getOptionValue(TEST_SUITE));

				TestSuiteExecutor tse = new TestSuiteExecutor(commandLine.getOptionValue(TEST_SUITE));
				tse.runTestSuite();
			}

		} catch (ParseException e) {
			HelpFormatter formatter = new HelpFormatter();
			formatter.printHelp("owlunit", options);
		}

	}
}
