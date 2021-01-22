package it.cnr.istc.stlab.owlunit.test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

import java.net.MalformedURLException;
import java.net.URISyntaxException;

import org.junit.Test;

import it.cnr.istc.stlab.owlunit.workers.TestSuiteExecutor;

public class TestSuiteExecutorTest {

	@Test
	public void testSuiteExecutor1() {
		TestSuiteExecutor tse = new TestSuiteExecutor("src/main/resources/testResources/testSuiteExecutor1.ttl");
		try {
			assertEquals(3, tse.runTestSuite());
		} catch (URISyntaxException e) {
			e.printStackTrace();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
	}
}
