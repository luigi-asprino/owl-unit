package it.cnr.istc.stlab.owlunit.workers;

import org.apache.jena.ontology.OntModel;
import org.apache.jena.query.Query;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.NodeIterator;
import org.apache.jena.riot.RDFDataMgr;

import it.cnr.istc.stlab.owlunit.Constants;

public abstract class TestWorkerBase implements TestWorker {

	protected String testCaseIRI;
	protected Model model;

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
			throw new OWLUnitException("No data input declared!");
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
		NodeIterator ni = model.listObjectsOfProperty(model.getResource(testCaseIRI),
				model.getProperty(Constants.OWLUNIT_TESTSONTOLOGY));

		if(!ni.hasNext()) {
			return null;
		}
		
		String ontologyURI = ni.next().asResource().getURI();
		OntModel om = ModelFactory.createOntologyModel();
		RDFDataMgr.read(om, ontologyURI);

		return om;
	}

}
