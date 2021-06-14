package it.cnr.istc.stlab.owlunit.test;

import static org.junit.Assert.assertTrue;

import org.junit.Ignore;
import org.junit.Test;

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
			tc.setFileIn(fileIn);
			assertTrue(tc.runTest());
		} catch (OWLUnitException e) {
			e.printStackTrace();
		}
	}

}
