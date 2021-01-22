package it.cnr.istc.stlab.owlunit.workers;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.reasoner.ConsoleProgressMonitor;
import org.semanticweb.owlapi.reasoner.OWLReasoner;
import org.semanticweb.owlapi.reasoner.OWLReasonerConfiguration;
import org.semanticweb.owlapi.reasoner.SimpleConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Utils {

	private static final Logger logger = LoggerFactory.getLogger(Utils.class);

	public static URI resolveLocation(String urlLocation) throws URISyntaxException {
		try {
			return new URL(urlLocation).toURI();
		} catch (MalformedURLException u) {
			logger.trace("Malformed url interpreting as file");
			return new File(urlLocation).toURI();
		}
	}

	public static String resolveLocationString(String urlLocation) throws URISyntaxException {
		try {
			return new URL(urlLocation).toURI().toString().replace("file:/", "file:///");
		} catch (MalformedURLException u) {
			logger.trace("Malformed url interpreting as file");
			return new File(urlLocation).toURI().toString().replace("file:/", "file:///");
		}
	}

	static boolean checkConsistency(String iriTestCase) throws OWLOntologyCreationException {

		OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
		OWLOntology ontology = manager.loadOntology(IRI.create(iriTestCase.toString()));

		ontology.importsClosure().forEach(ont -> {
			logger.trace("Importing " + ont.getOntologyID().getOntologyIRI().toString());
			ontology.addAxioms(ont.axioms());
		});

		OWLDataFactory factory = manager.getOWLDataFactory();

		ontology.annotations().forEach(a -> {
			if (a.containsEntityInSignature(factory.getOWLAnnotationProperty(
					"http://www.ontologydesignpatterns.org/schemas/testannotationschema.owl#hasInputTestData"))) {
				try {
					logger.trace("Importing " + a.getValue().toString());
					OWLOntology toyDataset = manager.loadOntology(IRI.create(a.getValue().toString()));
					toyDataset.importsClosure().forEach(ont -> {
						logger.trace("Importing " + ont.getOntologyID().getOntologyIRI().toString());
						ontology.addAxioms(ont.axioms());
					});
					ontology.addAxioms(toyDataset.axioms());
				} catch (OWLOntologyCreationException e) {
					e.printStackTrace();
				}
			}
		});

		ConsoleProgressMonitor progressMonitor = new ConsoleProgressMonitor();
		OWLReasonerConfiguration config = new SimpleConfiguration(progressMonitor);
		OWLReasoner reasoner = new org.semanticweb.HermiT.ReasonerFactory().createReasoner(ontology, config);

		return reasoner.isConsistent();

	}

	static Model getFalse(String iri) {
		Model m = ModelFactory.createDefaultModel();
		m.addLiteral(m.createResource(iri),
				m.createProperty(
						"http://www.ontologydesignpatterns.org/schemas/testannotationschema.owl#hasActualResult"),
				false);
		return m;
	}

	static Model getTrue(String iri) {
		Model m = ModelFactory.createDefaultModel();
		m.addLiteral(m.createResource(iri),
				m.createProperty(
						"http://www.ontologydesignpatterns.org/schemas/testannotationschema.owl#hasActualResult"),
				true);
		return m;
	}

}
