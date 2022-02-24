package it.cnr.istc.stlab.owlunit.workers;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.jena.graph.Graph;
import org.apache.jena.graph.Node;
import org.apache.jena.ontology.OntModel;
import org.apache.jena.query.Query;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.NodeIterator;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.Statement;
import org.apache.jena.rdf.model.StmtIterator;
import org.apache.jena.riot.Lang;
import org.apache.jena.riot.RDFDataMgr;
import org.apache.jena.riot.RiotException;
import org.apache.jena.sparql.graph.GraphFactory;
import org.apache.jena.vocabulary.OWL;
import org.apache.jena.vocabulary.RDF;
import org.semanticweb.owlapi.model.OWLOntologyIRIMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import it.cnr.istc.stlab.owlunit.Constants;

public abstract class TestWorkerBase implements TestWorker {

	protected String testCaseIRI;
	protected Model model = ModelFactory.createDefaultModel();
	protected String fileIn;
	protected List<OWLOntologyIRIMapper> mappers = new ArrayList<>();

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

	public void addMapper(OWLOntologyIRIMapper mapper) {
		this.mappers.add(mapper);
	}

	public void setMappers(List<OWLOntologyIRIMapper> mappers) {
		this.mappers = mappers;
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

		logger.trace("SPARQL query {}", sparqlQuery);

		Query q = QueryFactory.create(sparqlQuery);

		return q;
	}

	protected OntModel getTestedOntology() throws OWLUnitException {
		return getTestedOntology(true);
	}

	protected OntModel getTestedOntology(boolean loadimport) throws OWLUnitException {

		List<String> testedOntologyIRIs = getTestedOntologyIRIs();
		if (testedOntologyIRIs == null || testedOntologyIRIs.isEmpty()) {
			return null;
		}

		OntModel om = ModelFactory.createOntologyModel();
		for (String ontologyURI : testedOntologyIRIs) {
			try {
				logger.trace("Reading {}", ontologyURI);
				RDFDataMgr.read(om, ontologyURI);
			} catch (RiotException e) {
				try {
					RDFDataMgr.read(om, ontologyURI, Lang.RDFXML);
				} catch (RiotException e1) {
					throw new OWLUnitException(
							String.format("Syntax error found in ontology %s: %s", ontologyURI, e1.getMessage()));
				}
			}
		}

		if (loadimport) {
			om.loadImports();
		}

		logger.trace("Tested ontology size {}", om.size());

		return om;
	}

	protected Model getTestedOntologyAsModel() throws OWLUnitException {

		List<String> testedOntologyIRIs = getTestedOntologyIRIs();
		if (testedOntologyIRIs == null || testedOntologyIRIs.isEmpty()) {
			return null;
		}

		Model om = ModelFactory.createDefaultModel();
		for (String ontologyURI : testedOntologyIRIs) {
			try {
				RDFDataMgr.read(om, ontologyURI);
			} catch (RiotException e) {
				RDFDataMgr.read(om, ontologyURI, Lang.RDFXML);
			}
		}

		logger.trace("Tested ontology size {}", om.size());

		return om;
	}

	protected List<String> getTestedOntologyIRIs() throws OWLUnitException {

		if (logger.isTraceEnabled()) {
			model.write(System.out, "TTL");
		}

		NodeIterator ni = model.listObjectsOfProperty(model.getResource(testCaseIRI),
				model.getProperty(Constants.OWLUNIT_TESTSONTOLOGY));

		List<String> result = new ArrayList<>();

		if (!ni.hasNext()) {
			ni = model.listObjectsOfProperty(model.getResource(testCaseIRI), OWL.imports);
		}

		if (!ni.hasNext()) {
			logger.trace("Return null");
			return null;
		}

		while (ni.hasNext()) {
			result.add(ni.next().asResource().getURI());
		}

		logger.trace("Tested ontology iris {}", result);

		return result;
	}

	protected Object getExpectedResult() throws OWLUnitException {
		NodeIterator ni = model.listObjectsOfProperty(model.getResource(testCaseIRI),
				model.getProperty(Constants.TESTANNOTATIONSCHEMA_HASEXPECTEDRESULT));

		if (!ni.hasNext()) {
			ni = model.listObjectsOfProperty(model.getResource(testCaseIRI),
					model.getProperty(Constants.OWLUNIT_HASEXPECTEDRESULT));
		}

		if (!ni.hasNext()) {
			throw new OWLUnitException("No expected result declared");
		}

		RDFNode n = ni.next();

		if (n.isLiteral()) {
			logger.trace("Expected result Is Literal");
			return n.asLiteral().getString();
		} else {
			logger.trace("Expected result is not literal");
			Graph g = GraphFactory.createGraphMem();

			Graph g1 = model.getGraph();

			Set<Node> nodeToExplore = new HashSet<>();
			Set<Node> alreadyExplored = new HashSet<>();
			nodeToExplore.add(n.asNode());

			while (!nodeToExplore.isEmpty()) {
				Node nn = nodeToExplore.iterator().next();
				nodeToExplore.remove(nn);
				g1.find(nn, null, null).forEachRemaining(t -> {
					g.add(t);
					if (t.getObject().isURI() || t.getObject().isBlank()) {
						if (!alreadyExplored.contains(t.getObject())) {
							nodeToExplore.add(t.getObject());
						}
					}
				});
			}

//			g1.find(n.asNode(), null, null)

			return ModelFactory.createModelForGraph(g);
		}

	}

	public static List<TestWorker> guessTestClass(String iri, String uriTest, List<OWLOntologyIRIMapper> mappers)
			throws OWLUnitException {
		Model model = ModelFactory.createDefaultModel();
		RDFDataMgr.read(model, uriTest);

		List<TestWorker> result = new ArrayList<>();

		StmtIterator it2 = model.getResource(iri).listProperties(RDF.type);
		while (it2.hasNext()) {
			Statement s2 = (Statement) it2.next();
			result.add(getTest(s2.getSubject().asResource(), s2.getObject().asResource(), mappers));
		}

		return result;
	}

	private static TestWorker getTest(Resource iriTestCase, Resource klass, List<OWLOntologyIRIMapper> mappers)
			throws OWLUnitException {
		if (klass.getURI().equals(Constants.CQVERIFICATION)) {
			CompetencyQuestionVerificationExecutor cqtw = new CompetencyQuestionVerificationExecutor(
					iriTestCase.getURI());
			cqtw.setMappers(mappers);
			return cqtw;
		} else if (klass.getURI().equals(Constants.ERRORPROVOCATION)) {
			ErrorProvocationTestExecutor cqtw = new ErrorProvocationTestExecutor(iriTestCase.getURI());
			cqtw.setMappers(mappers);
			return cqtw;
		} else if (klass.getURI().equals(Constants.INFERENCEVERIFICATION)) {
			InferenceVerificationTestExecutor te = new InferenceVerificationTestExecutor(iriTestCase.getURI());
			te.setMappers(mappers);
			return te;
		} else if (klass.getURI().equals(Constants.ANNOTATIONVERIFICATION)) {
			AnnotationVerificationExecutor te = new AnnotationVerificationExecutor(iriTestCase.getURI());
			te.setMappers(mappers);
			return te;
		}

		throw new OWLUnitException("Unrecognized test case! class: " + klass.getURI());
	}

}
