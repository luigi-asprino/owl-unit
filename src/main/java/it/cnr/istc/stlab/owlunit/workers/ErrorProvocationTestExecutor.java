package it.cnr.istc.stlab.owlunit.workers;

import java.util.List;
import java.util.stream.Collectors;

import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.reasoner.OWLReasoner;
import org.semanticweb.owlapi.reasoner.OWLReasonerConfiguration;
import org.semanticweb.owlapi.reasoner.SimpleConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ErrorProvocationTestExecutor extends TestWorkerBase {

	private Logger logger = LoggerFactory.getLogger(ErrorProvocationTestExecutor.class);

	public ErrorProvocationTestExecutor(String iriTestCase) {
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

			for (OWLOntology ont : ontology.importsClosure().collect(Collectors.toList())) {
				logger.trace("Importing " + ont.getOntologyID().getOntologyIRI().toString());
				ontology.addAxioms(ont.axioms());
			}

			logger.trace("Ontology axioms {}", ontology.axioms().count());

			String inputTestData = getInputTestData();
			logger.trace("inputTestData: " + inputTestData);

			if (inputTestData != null) {
				logger.trace("Loading input data");
				try {
					OWLOntology toyDataset = manager.loadOntology(IRI.create(inputTestData));

					for (OWLOntology ont : toyDataset.importsClosure().collect(Collectors.toList())) {
						logger.trace("Importing " + ont.getOntologyID().getOntologyIRI().toString());
						ontology.addAxioms(ont.axioms());
					}

					logger.trace("Toy dataset axioms {}", toyDataset.axioms().count());
					ontology.addAxioms(toyDataset.axioms());
				} catch (OWLOntologyCreationException e) {
					e.printStackTrace();
				}
				logger.trace("Ontology axioms {}", ontology.axioms().count());
			} else {
				throw new OWLUnitException("No input data provided!");
			}

			OWLReasonerConfiguration config = new SimpleConfiguration();
			OWLReasoner reasoner = new org.semanticweb.HermiT.ReasonerFactory().createReasoner(ontology, config);

			return reasoner.isConsistent() == false;
		} catch (OWLOntologyCreationException e1) {
			throw new OWLUnitException(e1.getMessage());
		}
	}

}
