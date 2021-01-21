package it.cnr.istc.stlab.owlunit.workers;

import java.io.ByteArrayOutputStream;

import org.apache.jena.query.Query;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.ResultSetFormatter;
import org.apache.jena.query.Syntax;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.NodeIterator;
import org.apache.jena.riot.RDFDataMgr;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import it.cnr.istc.stlab.owlunit.Constants;

public class CompetencyQuestionVerificationExecutor extends TestWorkerBase {

	private static final Logger logger = LoggerFactory.getLogger(CompetencyQuestionVerificationExecutor.class);

	private enum Input {
		SPARQL_ENDPOINT, TOY_DATASET
	}

	public CompetencyQuestionVerificationExecutor(String testCaseIRI) {
		super();
		super.testCaseIRI = testCaseIRI;
		super.model = ModelFactory.createDefaultModel();
	}

	public boolean runTest() throws OWLUnitException {
		RDFDataMgr.read(model, testCaseIRI);

		logger.trace("Number of triples in test case " + model.size());

		Input i = getInputType();

		Query sparqlQuery = getSPARQLQuery();
		logger.trace("SPARQL query: " + sparqlQuery.toString(Syntax.syntaxSPARQL_11));

		if (i == null) {
			throw new OWLUnitException("No input type provided!");
		} else {

			String expectedResult = getExpectedResult().replaceAll("\\\\\"", "\"");
			logger.trace("Expected result: " + expectedResult);

			String inputTestData = getInputTestData();
			logger.trace("SPARQL endpoint: " + inputTestData);

			QueryExecution qexec = null;
			switch (i) {
			case SPARQL_ENDPOINT:
				logger.trace("SPARQL");
				qexec = QueryExecutionFactory.sparqlService(inputTestData, sparqlQuery);
				break;
			case TOY_DATASET:
				logger.trace("TOY " + inputTestData);
				Model m = ModelFactory.createDefaultModel();
				RDFDataMgr.read(m, inputTestData);
				qexec = QueryExecutionFactory.create(sparqlQuery, m);
			}
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ResultSetFormatter.outputAsJSON(baos, qexec.execSelect());

			logger.trace("Actual " + baos.toString());

			JsonElement expected = JsonParser.parseString(expectedResult);
			JsonElement actual = JsonParser.parseString(baos.toString());
			return expected.equals(actual);

		}

	}

	private Input getInputType() throws OWLUnitException {
		NodeIterator ni = model.listObjectsOfProperty(model.getResource(testCaseIRI),
				model.getProperty(Constants.OWLUNIT_ONTOLOGY_PREFIX + "hasInputTestDataCategory"));

		if (!ni.hasNext()) {
			ni = model.listObjectsOfProperty(model.getResource(testCaseIRI),
					model.getProperty(Constants.TESTALOD_ONTOLOGY_PREFIX + "hasInputTestDataCategory"));
		}

		if (!ni.hasNext()) {
			ni = model.listObjectsOfProperty(model.getResource(testCaseIRI),
					model.getProperty(Constants.TESTALOD_ONTOLOGY_OLD_PREFIX + "hasInputTestDataCategory"));
		}
		
		if(!ni.hasNext()) {
			return null;
		}

		String inputDataCategory = ni.next().asResource().getURI();
		logger.trace("Input Datata Category: " + inputDataCategory);

		if (inputDataCategory.equals(Constants.SPARQLENDPOINT_DATACATEGORY)
				|| inputDataCategory.equals(Constants.SPARQLENDPOINT_DATACATEGORY_OLD)
				|| inputDataCategory.equals(Constants.SPARQLENDPOINT_DATACATEGORY_TESTALOD)) {
			return Input.SPARQL_ENDPOINT;
		} else if (inputDataCategory.equals(Constants.TOY_DATASET_DATACATEGORY)
				|| inputDataCategory.equals(Constants.TOY_DATASET_DATACATEGORY_OLD)
				|| inputDataCategory.equals(Constants.TOY_DATASET_DATACATEGORY_TESTALOD)) {
			return Input.TOY_DATASET;
		}
		return null;

	}

	private String getExpectedResult() throws OWLUnitException {
		NodeIterator ni = model.listObjectsOfProperty(model.getResource(testCaseIRI),
				model.getProperty(Constants.TESTANNOTATIONSCHEMA_HASEXPECTEDRESULT));

		if (!ni.hasNext()) {
			throw new OWLUnitException("No expected result declared");
		}

		return ni.next().asLiteral().getString();

	}

	public static void main(String[] args) throws OWLUnitException {
		CompetencyQuestionVerificationExecutor cqw = new CompetencyQuestionVerificationExecutor(
				"https://w3id.org/arco/test/CQ/CQ_BNB1111.owl");

		System.out.println(cqw.runTest());

	}

}
