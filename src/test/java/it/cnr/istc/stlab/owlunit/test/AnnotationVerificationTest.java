package it.cnr.istc.stlab.owlunit.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import it.cnr.istc.stlab.owlunit.workers.AnnotationVerificationExecutor;
import it.cnr.istc.stlab.owlunit.workers.OWLUnitException;

public class AnnotationVerificationTest {

	@Test
	public void test1() {
		
		AnnotationVerificationExecutor at = new AnnotationVerificationExecutor("https://w3id.org/OWLunit/test/test1");
		at.setFileIn("src/main/resources/testResources/annotationVerification/test1.ttl");
		
		try {
			assertTrue(at.runTest());
		} catch (OWLUnitException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void test2() {
		AnnotationVerificationExecutor at = new AnnotationVerificationExecutor("https://w3id.org/OWLunit/test/test2");
		at.setFileIn("src/main/resources/testResources/annotationVerification/test2.ttl");
		
		try {
			assertFalse(at.runTest());
		} catch (OWLUnitException e) {
			e.printStackTrace();
		}
	}

}
