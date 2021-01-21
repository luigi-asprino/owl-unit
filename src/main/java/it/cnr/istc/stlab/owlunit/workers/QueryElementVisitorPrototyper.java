package it.cnr.istc.stlab.owlunit.workers;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.jena.graph.Graph;
import org.apache.jena.graph.Node;
import org.apache.jena.graph.NodeFactory;
import org.apache.jena.graph.Triple;
import org.apache.jena.ontology.OntModel;
import org.apache.jena.query.Query;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.ResultSet;
import org.apache.jena.sparql.core.Var;
import org.apache.jena.sparql.engine.binding.Binding;
import org.apache.jena.sparql.graph.GraphFactory;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class QueryElementVisitorPrototyper implements ElementVisitor {

	private Graph model = GraphFactory.createDefaultGraph();
	private String prefix = "https://w3id.org/owlunit/prototype/";
	private List<Binding> bindings;
	private Set<Var> buondVariables = new HashSet<>();
	private static final Logger logger = LoggerFactory.getLogger(QueryElementVisitorPrototyper.class);

	public void setBindings(OntModel o, Query q) {
		logger.debug("Set Bindings");
		QueryExecution qexec = QueryExecutionFactory.create(q, o);
		bindings = new ArrayList<>();
		ResultSet rs = qexec.execSelect();
		while (rs.hasNext()) {
			logger.debug("Binding ");
			Binding b = rs.nextBinding();
			b.vars().forEachRemaining(v -> {
				logger.debug(v.getName());
				buondVariables.add(v);
			});
			bindings.add(b);

		}
	}

	public Graph getGraph() {
		return model;
	}

	public void setPrefix(String prefix) {
		this.prefix = prefix;
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
		el.getOptionalElement().visit(this);
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
			logger.debug("Triple {}",t.toString());
			if (!hasBoundVariable(t.getSubject(), t.getPredicate(), t.getObject())) {
				this.model.add(new Triple(getNode(t.getSubject()), getNode(t.getPredicate()), getNode(t.getObject())));
			} else {
				logger.debug("The triple has bindings");
				for (Binding b : bindings) {
					logger.debug("Binding {}", b.toString());
					if (bindingHasVariable(b, t)) {
						logger.debug("Binding {} has {}", b.toString(), t.toString());
						this.model.add(getTripleWithBinding(t, b));
					}
				}
			}
		});

	}

	private Triple getTripleWithBinding(Triple t, Binding b) {
		return new Triple(getNode(t.getSubject(), b), getNode(t.getPredicate(), b), getNode(t.getObject(), b));
	}

	private boolean bindingHasVariable(Binding b, Triple t) {
		if (bindingHasVariable(b, t.getSubject()) || bindingHasVariable(b, t.getPredicate())
				|| bindingHasVariable(b, t.getObject())) {
			return true;
		}
		return false;
	}

	private boolean bindingHasVariable(Binding b, Node n) {
		return n.isVariable() && b.contains((Var) n);
	}

	private Node getNode(Node n) {
		if (n.isVariable()) {
			return NodeFactory.createURI(prefix + n.getName());
		}
		return n;
	}

	private Node getNode(Node n, Binding b) {
		Node s;
		if (bindingHasVariable(b, n)) {
			s = b.get((Var) n);
		} else {
			s = getNode(n);
		}

		return s;
	}

	private boolean hasBoundVariable(Node... nodes) {
		boolean result = false;
		for (Node n : nodes) {
			if (buondVariables.contains(n)) {
				result = true;
				break;
			}
		}
		return result;
	}

	@Override
	public void visit(ElementTriplesBlock el) {
		// TODO Auto-generated method stub

	}

	@Override
	public void visit(ElementFind el) {
		// TODO Auto-generated method stub

	}

}
