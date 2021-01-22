package it.cnr.istc.stlab.owlunit.workers;

import java.io.ByteArrayOutputStream;

import org.apache.jena.ontology.OntModel;
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
		loadTest();

		logger.trace("Number of triples in test case " + model.size());

		Input i = getInputType();
		if (i != null) {
			logger.trace("Input {}", i.toString());
		}

		Query sparqlQuery = getSPARQLQuery();
		logger.trace("SPARQL query: " + sparqlQuery.toString(Syntax.syntaxSPARQL_11));

		OntModel om = getTestedOntology();
		if (om != null) {
			QueryElementVisitorIRIExistenceVerifier qeviev = new QueryElementVisitorIRIExistenceVerifier(om);
			sparqlQuery.getQueryPattern().visit(qeviev);
			if (!qeviev.getResult()) {
				return false;
			}
//			QueryElementVisitorPrototyper qevp = new QueryElementVisitorPrototyper();
//			sparqlQuery.getQueryPattern().visit(qevp);
//			Model m = ModelFactory.createModelForGraph(qevp.getGraph());
//			om.add(m);
//
//			OntologyManager manager = OntManagers.createManager();
//			Ontology ontology = manager.addOntology(om.getGraph());
//
//			Configuration c = new Configuration();
//			c.ignoreUnsupportedDatatypes = true;
//			OWLReasoner reasoner = new org.semanticweb.HermiT.ReasonerFactory().createReasoner(ontology, c);
//
//			boolean consistent = reasoner.isConsistent();
//
//			if (!consistent) {
//				return false;
//			}

		}

		if (i != null) {

			String expectedResult = getExpectedResult().replaceAll("\\\\\"", "\"");
			logger.trace("Expected result: " + expectedResult);

			String inputTestData = getInputTestData();

			if (inputTestData == null) {
				throw new OWLUnitException("No data input declared!");
			}

			logger.trace("inputTestData: " + inputTestData);

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
			logger.trace("Expected " + expectedResult);

			JsonElement expected = JsonParser.parseString(expectedResult);
			JsonElement actual = JsonParser.parseString(baos.toString());
			return expected.equals(actual);

		}
		return true;
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

		if (!ni.hasNext()) {
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

	

}
