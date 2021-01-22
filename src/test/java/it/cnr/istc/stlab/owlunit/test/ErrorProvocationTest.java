package it.cnr.istc.stlab.owlunit.test;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import it.cnr.istc.stlab.owlunit.workers.ErrorProvocationTestExecutor;
import it.cnr.istc.stlab.owlunit.workers.OWLUnitException;

public class ErrorProvocationTest {

	@Test
	public void test8() {
		String URItest = "https://w3id.org/OWLunit/test/test8";
		String fileIn = "src/main/resources/testResources/test8.ttl";
		ErrorProvocationTestExecutor tc = new ErrorProvocationTestExecutor(URItest);
		tc.setFileIn(fileIn);
		try {
			assertTrue(tc.runTest());
		} catch (OWLUnitException e) {
			e.printStackTrace();
		}
	}

}
