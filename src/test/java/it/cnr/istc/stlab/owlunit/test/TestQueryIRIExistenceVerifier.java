package it.cnr.istc.stlab.owlunit.test;

import static org.junit.Assert.assertTrue;

import org.apache.jena.ontology.OntModel;
import org.apache.jena.query.Query;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.rdf.model.ModelFactory;
import org.junit.Test;

import it.cnr.istc.stlab.owlunit.workers.QueryElementVisitorIRIExistenceVerifier;

public class TestQueryIRIExistenceVerifier {

	private static final String TEST_PREFIX = "https://example.org/";

	@Test
	public void test1() {
		OntModel o = ModelFactory.createOntologyModel();
		o.createClass(TEST_PREFIX + "c");
		QueryElementVisitorIRIExistenceVerifier qeviev = new QueryElementVisitorIRIExistenceVerifier(o,null,null);
		Query q = QueryFactory.create("SELECT * {?s a <" + TEST_PREFIX + "c> }");
		q.getQueryPattern().visit(qeviev);
		assertTrue(qeviev.getResult());
	}
	
	@Test
	public void test2() {
		OntModel o = ModelFactory.createOntologyModel();
		o.createClass(TEST_PREFIX + "c");
		QueryElementVisitorIRIExistenceVerifier qeviev = new QueryElementVisitorIRIExistenceVerifier(o, null,null);
		Query q = QueryFactory.create("SELECT * {?s a <" + TEST_PREFIX + "d> }");
		q.getQueryPattern().visit(qeviev);
		System.out.println(qeviev.getResult());
		assertTrue(!qeviev.getResult());
	}
}
