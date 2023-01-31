package it.cnr.istc.stlab.owlunit.workers;

import java.util.ArrayList;
import java.util.List;

import org.apache.jena.graph.Graph;
import org.apache.jena.graph.Node;
import org.apache.jena.graph.Triple;
import org.apache.jena.ontology.OntModel;
import org.apache.jena.ontology.OntResource;
import org.apache.jena.riot.RDFDataMgr;
import org.apache.jena.sparql.graph.GraphFactory;
import org.apache.jena.sparql.syntax.ElementAssign;
import org.apache.jena.sparql.syntax.ElementBind;
import org.apache.jena.sparql.syntax.ElementData;
import org.apache.jena.sparql.syntax.ElementDataset;
import org.apache.jena.sparql.syntax.ElementExists;
import org.apache.jena.sparql.syntax.ElementFilter;
import org.apache.jena.sparql.syntax.ElementGroup;
import org.apache.jena.sparql.syntax.ElementMinus;
import org.apache.jena.sparql.syntax.ElementNamedGraph;
import org.apache.jena.sparql.syntax.ElementNotExists;
import org.apache.jena.sparql.syntax.ElementOptional;
import org.apache.jena.sparql.syntax.ElementPathBlock;
import org.apache.jena.sparql.syntax.ElementService;
import org.apache.jena.sparql.syntax.ElementSubQuery;
import org.apache.jena.sparql.syntax.ElementTriplesBlock;
import org.apache.jena.sparql.syntax.ElementUnion;
import org.apache.jena.sparql.syntax.ElementVisitor;
import org.apache.jena.vocabulary.RDF;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import it.cnr.istc.stlab.owlunit.OWLUnit;
import it.cnr.istc.stlab.owlunit.workers.CompetencyQuestionVerificationExecutor.Input;

public class QueryElementVisitorIRIExistenceVerifier implements ElementVisitor {

	private OntModel o;
	private boolean result = true;
	private Input i;
	private String inputTestData;
	private static final Logger logger = LoggerFactory.getLogger(OWLUnit.class);
	private Graph m;
	private List<String> urisNotFound = new ArrayList<>();

	public QueryElementVisitorIRIExistenceVerifier(OntModel o, String inputTestData, Input i) {
		this.o = o;
		this.inputTestData = inputTestData;
		this.i = i;

		if (this.i == Input.TOY_DATASET) {
			m = GraphFactory.createGraphMem();
			RDFDataMgr.read(m, inputTestData);
		}

	}

	public List<String> getURIsNotFound() {
		return urisNotFound;
	}

	@Override
	public void visit(ElementSubQuery el) {
		// Ignored
	}

	@Override
	public void visit(ElementService el) {
		// Ignored

	}

	@Override
	public void visit(ElementMinus el) {
		// Ignored

	}

	@Override
	public void visit(ElementNotExists el) {
		// Ignored

	}

	@Override
	public void visit(ElementExists el) {
		// Ignored

	}

	@Override
	public void visit(ElementNamedGraph el) {
		// Ignored

	}

	@Override
	public void visit(ElementDataset el) {
		// Ignored

	}

	@Override
	public void visit(ElementGroup el) {
		el.getElements().forEach(e -> {
			e.visit(this);
		});
	}

	@Override
	public void visit(ElementOptional el) {
		// Ignored

	}

	@Override
	public void visit(ElementUnion el) {
		// Ignored

	}

	@Override
	public void visit(ElementData el) {
		// Ignored

	}

	@Override
	public void visit(ElementBind el) {
		// Ignored

	}

	@Override
	public void visit(ElementAssign el) {
		// Ignored

	}

	@Override
	public void visit(ElementFilter el) {
		// Ignored

	}

	@Override
	public void visit(ElementPathBlock el) {
		el.getPattern().getList().forEach(tp -> {
			Triple t = tp.asTriple();
			if (!isDefinedInTheOntology(t.getSubject())) {
				result = false;
				urisNotFound.add(t.getSubject().getURI());
				logger.trace("Couldn't object {}", t.getSubject());
			}
			if (!isDefinedInTheOntology(t.getPredicate())) {
				result = false;
				urisNotFound.add(t.getPredicate().getURI());
				logger.trace("Couldn't object {}", t.getPredicate());
			}
			if (!isDefinedInTheOntology(t.getObject())) {
				result = false;
				urisNotFound.add(t.getObject().getURI());
				logger.trace("Couldn't object {}", t.getObject());
			}
		});
	}

	private boolean isDefinedInTheOntology(Node r) {
		if (!r.isURI()) {
			return true;
		}

		if (r.getNameSpace().equals(RDF.getURI())) {
			return true;
		}

		OntResource or = o.getOntResource(r.getURI());

		if (or == null) {
			logger.trace("Couldn't find {}", r.getURI());

			if (this.i == null) {
				logger.trace("Returning false");
				return false;
			}

			switch (this.i) {
			case SPARQL_ENDPOINT:
				throw new UnsupportedOperationException(
						"IRI verification in SPARQL endpoint not supported yet! " + inputTestData);
			case TOY_DATASET:
				boolean isIriInTheToyDataset = m.contains(r, null, null) || m.contains(null, r, null)
						|| m.contains(null, null, r);
				logger.trace("Is IRI in the Toy Dataset {}", isIriInTheToyDataset);
				return isIriInTheToyDataset;
			}
			return false;

		}

		return true;
	}

	@Override
	public void visit(ElementTriplesBlock el) {
		// Ignored

	}

}
