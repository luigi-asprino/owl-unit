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

import it.cnr.istc.stlab.owlunit.workers.CompetencyQuestionVerificationExecutor;
import it.cnr.istc.stlab.owlunit.workers.ErrorProvocationTestExecutor;
import it.cnr.istc.stlab.owlunit.workers.InferenceVerificationTestExecutor;
import it.cnr.istc.stlab.owlunit.workers.OWLUnitException;
import it.cnr.istc.stlab.owlunit.workers.TestSuiteExecutor;
import it.cnr.istc.stlab.owlunit.workers.TestWorker;
import it.cnr.istc.stlab.owlunit.workers.TestWorkerBase;

public class OWLUnit {

	private static final String TEST_SUITE = "s";
	private static final String TEST_CASE = "c";
	private static final Logger logger = LoggerFactory.getLogger(OWLUnit.class);

	public static void main(String[] args) throws OWLUnitException {

		Options options = new Options();

		options.addOption(Option.builder(TEST_SUITE).argName("URI").hasArg().required(false)
				.desc("The URI of the test suite to execute.").longOpt("test-suite").build());

		options.addOption(Option.builder(TEST_CASE).argName("URI").hasArg().required(false)
				.desc("The URI of the test case to execute.").longOpt("test-case").build());

		CommandLine commandLine = null;

		CommandLineParser cmdLineParser = new DefaultParser();
		try {
			commandLine = cmdLineParser.parse(options, args);

			if (!commandLine.hasOption(TEST_SUITE) && !commandLine.hasOption(TEST_CASE)) {
				printOptions(options);
			}

			if (commandLine.hasOption(TEST_SUITE)) {
				logger.info("Test suite URI {}", commandLine.getOptionValue(TEST_SUITE));

				TestSuiteExecutor tse = new TestSuiteExecutor(commandLine.getOptionValue(TEST_SUITE));
				tse.runTestSuite();
			}

			if (commandLine.hasOption(TEST_CASE)) {
				logger.info("Test case URI {}", commandLine.getOptionValue(TEST_CASE));

				for (TestWorker tw : TestWorkerBase.guessTestClass(commandLine.getOptionValue(TEST_CASE))) {
					if (tw.getClass().equals(CompetencyQuestionVerificationExecutor.class)) {
						System.out.print("CQ Verification test ");
					}

					if (tw.getClass().equals(ErrorProvocationTestExecutor.class)) {
						System.out.print("Error Provocation test ");
					}

					if (tw.getClass().equals(InferenceVerificationTestExecutor.class)) {
						System.out.print("Inference Verification test ");
					}

					if (tw.runTest()) {
						System.out.println("PASSED");
					} else {
						System.out.println("FAILED");
					}
				}

			}

		} catch (ParseException e) {
			printOptions(options);
		}
	}

	private static void printOptions(Options options) {
		HelpFormatter formatter = new HelpFormatter();
		formatter.printHelp("java -jar OWLUnit-0.0.4.jar [ARGS]", options);
	}

}
