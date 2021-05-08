package it.cnr.istc.stlab.owlunit.workers;

import java.util.List;

import org.apache.jena.query.Query;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.Syntax;
import org.apache.jena.rdf.model.Model;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.model.parameters.OntologyCopy;
import org.semanticweb.owlapi.reasoner.OWLReasoner;
import org.semanticweb.owlapi.reasoner.OWLReasonerConfiguration;
import org.semanticweb.owlapi.reasoner.SimpleConfiguration;
import org.semanticweb.owlapi.util.InferredOntologyGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.owlcs.ontapi.OntManagers;
import com.github.owlcs.ontapi.Ontology;
import com.github.owlcs.ontapi.OntologyManager;

public class InferenceVerificationTestExecutor extends TestWorkerBase {

	private Logger logger = LoggerFactory.getLogger(InferenceVerificationTestExecutor.class);

	public InferenceVerificationTestExecutor(String iriTestCase) {
		super.testCaseIRI = iriTestCase;
	}

	@Override
	public boolean runTest() throws OWLUnitException {

		loadTest();

		List<String> ontologyIRIs = getTestedOntologyIRIs();
		logger.trace("Ontology IRI to test {}", ontologyIRIs);

		OWLOntologyManager manager = OWLManager.createOWLOntologyManager();

		try {

			OWLOntology ontology = manager.createOntology();

			for (String ontologyIRI : ontologyIRIs) {
				ontology.addAxioms(manager.loadOntology(IRI.create(ontologyIRI)).axioms());
			}

			ontology.importsClosure().forEach(ont -> {
				logger.trace("Importing " + ont.getOntologyID().getOntologyIRI().toString());
				ontology.addAxioms(ont.axioms());
			});

			logger.trace("Ontology axioms {}", ontology.axioms().count());

			String inputTestData = getInputTestData();
			logger.trace("inputTestData: " + inputTestData);

			if (inputTestData != null) {
				logger.trace("Loading input data");
				try {
					OWLOntology toyDataset = manager.loadOntology(IRI.create(inputTestData));
					toyDataset.importsClosure().forEach(ont -> {
						logger.trace("Importing " + ont.getOntologyID().getOntologyIRI().toString());
						ontology.addAxioms(ont.axioms());
					});
					logger.trace("Toy dataset axioms {}", toyDataset.axioms().count());
					ontology.addAxioms(toyDataset.axioms());
				} catch (OWLOntologyCreationException e) {
					e.printStackTrace();
				}
				logger.trace("Ontology axioms {}", ontology.axioms().count());
			}

			OWLReasonerConfiguration config = new SimpleConfiguration();
			OWLReasoner reasoner = new org.semanticweb.HermiT.ReasonerFactory().createReasoner(ontology, config);

			OWLDataFactory factory = manager.getOWLDataFactory();
			InferredOntologyGenerator gen = new InferredOntologyGenerator(reasoner);
			OWLOntology newOntology;
			boolean isConsistent = false;
			try {
				newOntology = manager.createOntology();
				gen.fillOntology(factory, newOntology);
				OntologyManager ontManager = OntManagers.createManager();
				Ontology ontOntology = ontManager.copyOntology(newOntology, OntologyCopy.DEEP);
				ontology.axioms().forEach(a -> {
					ontOntology.addAxiom(a);
				});
				Model m = ((com.github.owlcs.ontapi.Ontology) ontOntology).asGraphModel();
				Query sparqlQuery = getSPARQLQuery();
				isConsistent = reasoner.isConsistent();
				if (!isConsistent) {
					return false;
				}

				if (sparqlQuery != null) {
					logger.trace("SPARQL query: " + sparqlQuery.toString(Syntax.syntaxSPARQL_11));
					QueryExecution qexec = QueryExecutionFactory.create(sparqlQuery, m);

					Object expectedResult = getExpectedResult();
					logger.trace("Expected result: " + expectedResult);

					if (expectedResult.getClass().equals(String.class)) {
						return Boolean.parseBoolean((String) expectedResult) == qexec.execAsk();
					} else {
						throw new OWLUnitException("Wrong type of the expected result! (Expected boolean)");
					}

				}
			} catch (OWLOntologyCreationException e) {
				e.printStackTrace();
			}

			return isConsistent;

		} catch (OWLOntologyCreationException e1) {
			throw new OWLUnitException(e1.getMessage());
		}
	}

}
