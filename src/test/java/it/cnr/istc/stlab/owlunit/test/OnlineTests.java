package it.cnr.istc.stlab.owlunit.test;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import it.cnr.istc.stlab.owlunit.workers.CompetencyQuestionVerificationExecutor;
import it.cnr.istc.stlab.owlunit.workers.ErrorProvocationTestExecutor;
import it.cnr.istc.stlab.owlunit.workers.InferenceVerificationTestExecutor;
import it.cnr.istc.stlab.owlunit.workers.OWLUnitException;

public class OnlineTests {

	@Test
	public void test1() {
		String URItest = "https://raw.githubusercontent.com/luigi-asprino/owl-unit/main/examples/cq1.ttl";
		CompetencyQuestionVerificationExecutor cqve = new CompetencyQuestionVerificationExecutor(URItest);
		try {
			assertTrue(cqve.runTest());
		} catch (OWLUnitException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void test2() {
		String URItest = "https://raw.githubusercontent.com/luigi-asprino/owl-unit/main/examples/cq1-testalod.ttl";
		CompetencyQuestionVerificationExecutor cqve = new CompetencyQuestionVerificationExecutor(URItest);
		try {
			assertTrue(cqve.runTest());
		} catch (OWLUnitException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void test3() {
		String URItest = "https://raw.githubusercontent.com/luigi-asprino/owl-unit/main/examples/iv.ttl";
		InferenceVerificationTestExecutor te = new InferenceVerificationTestExecutor(URItest);
		try {
			assertTrue(te.runTest());
		} catch (OWLUnitException e) {
			e.printStackTrace();
		}
	}
	
	
	@Test
	public void test4() {
		String URItest = "https://raw.githubusercontent.com/luigi-asprino/owl-unit/main/examples/ep.ttl";
		ErrorProvocationTestExecutor te = new ErrorProvocationTestExecutor(URItest);
		try {
			assertTrue(te.runTest());
		} catch (OWLUnitException e) {
			e.printStackTrace();
		}
	}

}