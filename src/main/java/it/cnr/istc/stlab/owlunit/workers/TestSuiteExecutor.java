package it.cnr.istc.stlab.owlunit.workers;

import java.net.URISyntaxException;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.Statement;
import org.apache.jena.rdf.model.StmtIterator;
import org.apache.jena.riot.RDFDataMgr;
import org.apache.jena.vocabulary.RDF;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import it.cnr.istc.stlab.owlunit.Constants;

public class TestSuiteExecutor {

	private static final Logger logger = LoggerFactory.getLogger(TestSuiteExecutor.class);

	private String iri;

	private String filepath;

	public TestSuiteExecutor(String iri) {
		this.iri = iri;
	}

	public void setFileIn(String path) {
		this.filepath = path;
	}

	public int runTestSuite() {
		Model m = ModelFactory.createDefaultModel();

		String location = filepath == null ? iri : filepath;

		logger.trace("Location {}", location);

		RDFDataMgr.read(m, location);
		logger.trace("IRI Executor model size {}", m.size());
		StmtIterator it = m.listStatements(m.getResource(iri), m.getProperty(Constants.HASTESTCASE),
				(RDFNode) null);
//		logger.trace("Number of test cases {}", it.toModel().size());
//		m.write(System.out, "NT");
		System.out.println("Test-Case URI\tResult");
		int testsRan = 0;
		while (it.hasNext()) {
			Statement s = it.next();
			logger.trace("Executing Test Case {}", s.getObject().asResource().getURI());
			StmtIterator it2 = s.getObject().asResource().listProperties(RDF.type);
			while (it2.hasNext()) {
				Statement s2 = (Statement) it2.next();
				try {
					boolean result = runTestByClass(s2.getSubject().asResource(), s2.getObject().asResource());
					// logger.info("{}\t{}", s2.getSubject().asResource().getURI(), result);
					if (result) {
						System.out.println(s2.getSubject().asResource().getURI() + "\tPassed");
					} else {
						System.out.println(s2.getSubject().asResource().getURI() + "\tFailed");
					}
					testsRan++;
				} catch (Exception e) {
					// logger.info("{}\t{}", s2.getSubject().asResource().getURI(), e.getMessage());
					System.err.println("Error " + s2.getSubject().asResource().getURI() + " " + e.getMessage());
					e.printStackTrace();

				}
			}
		}
		return testsRan;
	}

	private boolean runTestByClass(Resource iriTestCase, Resource klass) throws OWLUnitException, URISyntaxException {
		if (klass.getURI().equals(Constants.CQVERIFICATION)) {
			CompetencyQuestionVerificationExecutor cqtw = new CompetencyQuestionVerificationExecutor(
					iriTestCase.getURI());
			return cqtw.runTest();
		} else if (klass.getURI().equals(Constants.ERRORPROVOCATION)) {
			ErrorProvocationTestExecutor cqtw = new ErrorProvocationTestExecutor(iriTestCase.getURI());
			return cqtw.runTest();
		} else if (klass.getURI().equals(Constants.INFERENCEVERIFICATION)) {
			InferenceVerificationTestExecutor te = new InferenceVerificationTestExecutor(iriTestCase.getURI());
			return te.runTest();
		} else if (klass.getURI().equals(Constants.ANNOTATIONVERIFICATION)) {
			AnnotationVerificationExecutor te = new AnnotationVerificationExecutor(iriTestCase.getURI());
			return te.runTest();
		}
		throw new OWLUnitException("Unrecognized test case! class: " + klass.getURI());
	}

}
