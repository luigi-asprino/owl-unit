package it.cnr.istc.stlab.owlunit.workers;

import org.apache.jena.ontology.OntModel;
import org.apache.jena.query.Query;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.NodeIterator;
import org.apache.jena.riot.RDFDataMgr;
import org.apache.jena.vocabulary.OWL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import it.cnr.istc.stlab.owlunit.Constants;

public abstract class TestWorkerBase implements TestWorker {

	protected String testCaseIRI;
	protected Model model=ModelFactory.createDefaultModel();
	protected String fileIn;

	private Logger logger = LoggerFactory.getLogger(TestWorkerBase.class);

	public void setFileIn(String fi) {
		this.fileIn = fi;
	}

	public void loadTest() {
		if (fileIn == null) {
			logger.trace("Loaded using IRI {}", testCaseIRI);
			RDFDataMgr.read(model, testCaseIRI);
		} else {
			logger.trace("Loaded using path {}", fileIn);
			RDFDataMgr.read(model, fileIn);
		}
	}

	public String getInputTestData() throws OWLUnitException {
		NodeIterator ni = model.listObjectsOfProperty(model.getResource(testCaseIRI),
				model.getProperty(Constants.OWLUNIT_ONTOLOGY_PREFIX + "hasInputTestDataUri"));

		if (!ni.hasNext()) {
			ni = model.listObjectsOfProperty(model.getResource(testCaseIRI),
					model.getProperty(Constants.TESTALOD_ONTOLOGY_PREFIX + "hasInputTestDataUri"));
		}

		if (!ni.hasNext()) {
			ni = model.listObjectsOfProperty(model.getResource(testCaseIRI),
					model.getProperty(Constants.TESTALOD_ONTOLOGY_OLD_PREFIX + "hasInputTestDataUri"));
		}

		if (!ni.hasNext()) {
			ni = model.listObjectsOfProperty(model.getResource(testCaseIRI),
					model.getProperty(Constants.OWLUNIT_ONTOLOGY_PREFIX + "hasInputData"));
		}

		if (!ni.hasNext()) {
			return null;
		}

		return ni.next().asResource().getURI();
	}

	protected Query getSPARQLQuery() throws OWLUnitException {
		NodeIterator ni = model.listObjectsOfProperty(model.getResource(testCaseIRI),
				model.getProperty(Constants.TESTANNOTATIONSCHEMA_HASSPARQLQUERYUNITTEST));

		if (!ni.hasNext()) {
			ni = model.listObjectsOfProperty(model.getResource(testCaseIRI),
					model.getProperty(Constants.OWLUNIT_HASSPARQLQUERYUNITTEST));
		}

		if (!ni.hasNext()) {
			throw new OWLUnitException("No SPARQL query unit test declared");
		}

		String sparqlQuery = ni.next().asLiteral().getString();

		Query q = QueryFactory.create(sparqlQuery);

		return q;
	}

	protected OntModel getTestedOntology() throws OWLUnitException {

		String ontologyURI = getTestedOntologyIRI();
		if (ontologyURI == null) {
			return null;
		}

		OntModel om = ModelFactory.createOntologyModel();
		RDFDataMgr.read(om, ontologyURI);

		return om;
	}

	protected String getTestedOntologyIRI() throws OWLUnitException {
		NodeIterator ni = model.listObjectsOfProperty(model.getResource(testCaseIRI),
				model.getProperty(Constants.OWLUNIT_TESTSONTOLOGY));

		if (!ni.hasNext()) {
			ni = model.listObjectsOfProperty(model.getResource(testCaseIRI), OWL.imports);
		}

		if (!ni.hasNext()) {
			return null;
		}

		return ni.next().asResource().getURI();
	}
	
	protected String getExpectedResult() throws OWLUnitException {
		NodeIterator ni = model.listObjectsOfProperty(model.getResource(testCaseIRI),
				model.getProperty(Constants.TESTANNOTATIONSCHEMA_HASEXPECTEDRESULT));

		if (!ni.hasNext()) {
			ni = model.listObjectsOfProperty(model.getResource(testCaseIRI),
					model.getProperty(Constants.OWLUNIT_HASEXPECTEDRESULT));
		}

		if (!ni.hasNext()) {
			throw new OWLUnitException("No expected result declared");
		}

		return ni.next().asLiteral().getString();

	}

}
