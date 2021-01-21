package it.cnr.istc.stlab.owlunit.workers;

import org.apache.jena.graph.Node;
import org.apache.jena.graph.Triple;
import org.apache.jena.ontology.OntModel;
import org.apache.jena.ontology.OntResource;
import org.apache.jena.sparql.syntax.ElementAssign;
import org.apache.jena.sparql.syntax.ElementBind;
import org.apache.jena.sparql.syntax.ElementData;
import org.apache.jena.sparql.syntax.ElementDataset;
import org.apache.jena.sparql.syntax.ElementExists;
import org.apache.jena.sparql.syntax.ElementFilter;
import org.apache.jena.sparql.syntax.ElementFind;
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

public class QueryElementVisitorIRIExistenceVerifier implements ElementVisitor {

	private OntModel o;
	private boolean result = true;

	public QueryElementVisitorIRIExistenceVerifier(OntModel o) {
		this.o = o;
	}

	public boolean getResult() {
		return result;
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
			if (!isDefinedInTheOntology(t.getSubject()) || !isDefinedInTheOntology(t.getPredicate())
					|| !isDefinedInTheOntology(t.getObject())) {
				result = false;
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
		return or != null;
	}

	@Override
	public void visit(ElementTriplesBlock el) {
		// Ignored

	}

	@Override
	public void visit(ElementFind el) {
		// TODO Auto-generated method stub

	}

}