package it.cnr.istc.stlab.owlunit.test;

import static org.junit.Assert.assertTrue;

import org.apache.jena.graph.Graph;
import org.apache.jena.graph.NodeFactory;
import org.apache.jena.graph.Triple;
import org.apache.jena.ontology.OntClass;
import org.apache.jena.ontology.OntModel;
import org.apache.jena.query.ParameterizedSparqlString;
import org.apache.jena.query.Query;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.sparql.graph.GraphFactory;
import org.apache.jena.vocabulary.RDF;
import org.apache.jena.vocabulary.RDFS;
import org.junit.Test;

import it.cnr.istc.stlab.owlunit.workers.QueryElementVisitorPrototyper;

public class TestQueryPrototyper {

	private static final String TEST_PREFIX = "https://example.org/";

	@Test
	public void test1() {
		String s = "SELECT * {?s ?p ?o}";

		QueryElementVisitorPrototyper qevp = new QueryElementVisitorPrototyper();
		qevp.setPrefix(TEST_PREFIX);

		Query q = QueryFactory.create(s);
		q.getQueryPattern().visit(qevp);

		Graph g = GraphFactory.createDefaultGraph();

		g.add(createTripleWithTestPrefix("s", "p", "o"));

		assertTrue(g.isIsomorphicWith(qevp.getGraph()));
	}

	@Test
	public void test2() {
		String s = "SELECT * {?s a ?o}";

		QueryElementVisitorPrototyper qevp = new QueryElementVisitorPrototyper();
		qevp.setPrefix(TEST_PREFIX);

		Query q = QueryFactory.create(s);
		q.getQueryPattern().visit(qevp);

		Graph g = GraphFactory.createDefaultGraph();

		g.add(createTripleWithURIs(TEST_PREFIX + "s", RDF.type.getURI(), TEST_PREFIX + "o"));

		assertTrue(g.isIsomorphicWith(qevp.getGraph()));
	}

	@Test
	public void test3() {
		String s = "SELECT * {?s ?p ?o . ?o ?p1 ?o1}";

		QueryElementVisitorPrototyper qevp = new QueryElementVisitorPrototyper();
		qevp.setPrefix(TEST_PREFIX);

		Query q = QueryFactory.create(s);
		q.getQueryPattern().visit(qevp);

		Graph g = GraphFactory.createDefaultGraph();

		g.add(createTripleWithTestPrefix("s", "p", "o"));
		g.add(createTripleWithTestPrefix("o", "p1", "o1"));

		assertTrue(g.isIsomorphicWith(qevp.getGraph()));
	}

	@Test
	public void testBindings() {
		String s = "SELECT * {?subC1 rdfs:subClassOf ?c1 . OPTIONAL {?iC1 rdf:type ?subC1} }";
		ParameterizedSparqlString pss = new ParameterizedSparqlString(s);
		pss.setNsPrefix("rdf", RDF.uri);
		pss.setNsPrefix("rdfs", RDFS.uri);
		pss.setNsPrefix("test", TEST_PREFIX);
		pss.setIri("c1", TEST_PREFIX+"c1");
		OntModel om = ModelFactory.createOntologyModel();
		OntClass oc1 = om.createClass(TEST_PREFIX+"c1");
		OntClass oc2 = om.createClass(TEST_PREFIX+"c2");
		OntClass oc3 = om.createClass(TEST_PREFIX+"c3");
		oc1.addSubClass(oc2);
		oc1.addSubClass(oc3);

		QueryElementVisitorPrototyper qevp = new QueryElementVisitorPrototyper();
		Query q = pss.asQuery();
		qevp.setBindings(om, q);
		q.getQueryPattern().visit(qevp);

//		Graph g = GraphFactory.createDefaultGraph();
//		
		ModelFactory.createModelForGraph(qevp.getGraph()).write(System.out,"TTL");

//		g.add(createTripleWithTestPrefix("s", "p", "o"));
//		g.add(createTripleWithTestPrefix("o", "p1", "o1"));
//
//		assertTrue(g.isIsomorphicWith(qevp.getGraph()));
	}

	private static Triple createTripleWithTestPrefix(String s, String p, String o) {
		return new Triple(NodeFactory.createURI(TEST_PREFIX + s), NodeFactory.createURI(TEST_PREFIX + p),
				NodeFactory.createURI(TEST_PREFIX + o));
	}

	private static Triple createTripleWithURIs(String s, String p, String o) {
		return new Triple(NodeFactory.createURI(s), NodeFactory.createURI(p), NodeFactory.createURI(o));
	}
}
