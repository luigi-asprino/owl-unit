package it.cnr.istc.stlab.owlunit.workers;

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

	public TestSuiteExecutor(String iri) {
		this.iri = iri;
	}

	public void runTestSuite() {
		Model m = ModelFactory.createDefaultModel();
		RDFDataMgr.read(m, iri);
		StmtIterator it = m.listStatements(m.getResource(iri), m.getProperty(Constants.HASTESTCASE), (RDFNode) null);
		System.out.println("Test-Case URI\tResult");

		while (it.hasNext()) {
			Statement s = it.next();

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
				} catch (Exception e) {
					// logger.info("{}\t{}", s2.getSubject().asResource().getURI(), e.getMessage());
					System.err.println("Error " + s2.getSubject().asResource().getURI()+" "+e.getMessage());

				}
			}
		}
	}

	private boolean runTestByClass(Resource iriTestCase, Resource klass) throws OWLUnitException {
		if (klass.getURI().equals(Constants.CQVERIFICATION)) {
			CompetencyQuestionVerificationExecutor cqtw = new CompetencyQuestionVerificationExecutor(
					iriTestCase.getURI());
			return cqtw.runTest();
		} else if (klass.getURI().equals(Constants.ERRORPROVOCATION)) {
			// TODO
		} else if (klass.getURI().equals(Constants.INFERENCEVERIFICATION)) {
			// TODO
		}
		throw new OWLUnitException("Unrecognized test case! class: " + klass.getURI());
	}

}
