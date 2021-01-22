package it.cnr.istc.stlab.owlunit.test;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import it.cnr.istc.stlab.owlunit.workers.TestSuiteExecutor;

public class TestSuiteExecutorTest {

	@Test
	public void testSuiteExecutor1() {
		TestSuiteExecutor tse = new TestSuiteExecutor(
				"https://raw.githubusercontent.com/luigi-asprino/owl-unit/main/src/main/resources/testResources/testSuiteExecutor1.ttl");
		assertEquals(3, tse.runTestSuite());
	}
}
