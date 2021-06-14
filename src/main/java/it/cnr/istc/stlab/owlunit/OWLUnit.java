package it.cnr.istc.stlab.owlunit;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLOntologyIRIMapper;
import org.semanticweb.owlapi.util.SimpleIRIMapper;
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
	private static final String IRI_MAPPING = "m";
	private static final Logger logger = LoggerFactory.getLogger(OWLUnit.class);

	public static void main(String[] args) throws OWLUnitException {

		Options options = new Options();

		options.addOption(Option.builder(TEST_SUITE).argName("URI").hasArg().required(false)
				.desc("The URI of the test suite to execute.").longOpt("test-suite").build());

		options.addOption(Option.builder(TEST_CASE).argName("URI").hasArg().required(false)
				.desc("The URI of the test case to execute.").longOpt("test-case").build());

		Option o = Option.builder(IRI_MAPPING).argName("A list of pairs of IRIs").required(false).desc(
				"A list of pairs IRIs separated by a white space. The first IRI of the pair will be resolved on the second of the pair.")
				.longOpt("iri-mappings").build();
		o.setArgs(Option.UNLIMITED_VALUES);

		options.addOption(o);

		CommandLine commandLine = null;

		CommandLineParser cmdLineParser = new DefaultParser();
		try {
			commandLine = cmdLineParser.parse(options, args);

			if (!commandLine.hasOption(TEST_SUITE) && !commandLine.hasOption(TEST_CASE)) {
				printOptions(options);
			}

			List<OWLOntologyIRIMapper> mappers = new ArrayList<>();

			if (commandLine.hasOption(IRI_MAPPING)) {
				String[] pairs = commandLine.getOptionValues(IRI_MAPPING);
				for (int i = 0; i < pairs.length;) {
					mappers.add(new SimpleIRIMapper(IRI.create(pairs[i++]), IRI.create(pairs[i++])));
				}
			}

			if (commandLine.hasOption(TEST_SUITE)) {
				logger.info("Test suite URI {}", commandLine.getOptionValue(TEST_SUITE));

				TestSuiteExecutor tse = new TestSuiteExecutor(commandLine.getOptionValue(TEST_SUITE));
				tse.runTestSuite();
			}

			if (commandLine.hasOption(TEST_CASE)) {
				logger.info("Test case URI {}", commandLine.getOptionValue(TEST_CASE));

				for (TestWorker tw : TestWorkerBase.guessTestClass(commandLine.getOptionValue(TEST_CASE), mappers)) {
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
		formatter.printHelp("java -jar OWLUnit-<VERSION>.jar [ARGS]", options);
	}

}
