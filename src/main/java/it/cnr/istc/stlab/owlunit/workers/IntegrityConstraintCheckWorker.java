package it.cnr.istc.stlab.owlunit.workers;

import org.apache.jena.query.Query;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.Syntax;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.NodeIterator;
import org.apache.jena.riot.RDFDataMgr;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import it.cnr.istc.stlab.owlunit.Constants;

public class IntegrityConstraintCheckWorker extends TestWorkerBase {

	private static final Logger logger = LoggerFactory.getLogger(IntegrityConstraintCheckWorker.class);

	public IntegrityConstraintCheckWorker(String testCaseIRI) {
		super();
		super.testCaseIRI = testCaseIRI;
		super.model = ModelFactory.createDefaultModel();
	}

	public boolean runTest() throws OWLUnitException {
		RDFDataMgr.read(model, testCaseIRI);

		logger.trace("Number of triples in test case " + model.size());

		if (!checkDataInputType()) {
			throw new OWLUnitException("Integrity constraints check test cases can be run only on SPARQL endpoints");
		}

		String sparqlEndpointURI = getInputTestData();
		logger.trace("SPARQL endpoint: " + sparqlEndpointURI);

		Query sparqlQuery = getSPARQLQuery();
		logger.trace("SPARQL query: " + sparqlQuery.toString(Syntax.syntaxSPARQL_11));

		if (!sparqlQuery.isAskType()) {
			throw new OWLUnitException("Only ASK SPARQL query allowed");
		}

		boolean expectedResult = getExpectedResult();
		logger.trace("Expected result: " + expectedResult);

		QueryExecution qexec = QueryExecutionFactory.sparqlService(sparqlEndpointURI, sparqlQuery);

		return qexec.execAsk();
	}

	private boolean checkDataInputType() throws OWLUnitException {
		NodeIterator ni = model.listObjectsOfProperty(model.getResource(testCaseIRI),
				model.getProperty(Constants.OWLUNIT_ONTOLOGY_PREFIX + "hasInputTestDataCategory"));

		if (!ni.hasNext()) {
			ni = model.listObjectsOfProperty(model.getResource(testCaseIRI),
					model.getProperty(Constants.TESTALOD_ONTOLOGY_PREFIX + "hasInputTestDataCategory"));
		}

		if (!ni.hasNext()) {
			throw new OWLUnitException("No input data category declared");
		}

		String inputDataCategory = ni.next().asResource().getURI();
		logger.trace("Input Datata Category: " + inputDataCategory);

		return inputDataCategory.equals(Constants.SPARQLENDPOINT_DATACATEGORY)
				|| inputDataCategory.equals(Constants.SPARQLENDPOINT_DATACATEGORY_OLD)
				|| inputDataCategory.equals(Constants.SPARQLENDPOINT_DATACATEGORY_TESTALOD);
	}

	private boolean getExpectedResult() throws OWLUnitException {
		NodeIterator ni = model.listObjectsOfProperty(model.getResource(testCaseIRI),
				model.getProperty(Constants.TESTANNOTATIONSCHEMA_HASEXPECTEDRESULT));

		if (!ni.hasNext()) {
			throw new OWLUnitException("No expected result declared");
		}

		return ni.next().asLiteral().getBoolean();

	}

	public static void main(String[] args) {
		IntegrityConstraintCheckWorker ccw = new IntegrityConstraintCheckWorker(
				"https://w3id.org/arco/test/IC/testcase-01.owl");
		try {
			logger.info("Actual result " + ccw.runTest());
		} catch (OWLUnitException e) {
			e.printStackTrace();
		}
	}

}
