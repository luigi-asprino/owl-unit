package it.cnr.istc.stlab.owlunit.test;

import static org.junit.Assert.assertTrue;

import org.junit.Ignore;
import org.junit.Test;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.util.SimpleIRIMapper;

import it.cnr.istc.stlab.owlunit.workers.InferenceVerificationTestExecutor;
import it.cnr.istc.stlab.owlunit.workers.OWLUnitException;

public class InferenceVerificationTest {

	@Test
	public void test7() {
		String URItest = "https://w3id.org/OWLunit/test/test7";
		String fileIn = "src/main/resources/testResources/test7.ttl";
		try {
			InferenceVerificationTestExecutor tc = new InferenceVerificationTestExecutor(URItest);
			tc.setFileIn(fileIn);
			assertTrue(tc.runTest());
		} catch (OWLUnitException e) {
			e.printStackTrace();
		}
	}

	@Ignore
	@Test
	public void testMC() {
		String URItest = "https://raw.githubusercontent.com/mchiaraf/owl-unit-tests/main/test/iv2.ttl";
		String fileIn = "https://raw.githubusercontent.com/mchiaraf/owl-unit-tests/main/test/iv2.ttl";
		try {
			InferenceVerificationTestExecutor tc = new InferenceVerificationTestExecutor(URItest);
			tc.addMapper(new SimpleIRIMapper(IRI.create("https://w3id.org/arco/ontology/context-description/1.3"), IRI
					.create("https://raw.githubusercontent.com/ICCD-MiBACT/ArCo/DEV-1.3.0/ArCo-release/ontologie/context-description/1.3/context-description.owl")));
			tc.addMapper(new SimpleIRIMapper(IRI.create("https://w3id.org/arco/ontology/core/1.3"), IRI.create(
					"https://raw.githubusercontent.com/ICCD-MiBACT/ArCo/DEV-1.3.0/ArCo-release/ontologie/core/1.3/core.owl")));
			tc.setFileIn(fileIn);
			assertTrue(tc.runTest());
		} catch (OWLUnitException e) {
			e.printStackTrace();
		}
	}

}
