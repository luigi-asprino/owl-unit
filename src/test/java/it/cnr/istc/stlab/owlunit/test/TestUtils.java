package it.cnr.istc.stlab.owlunit.test;

import static org.junit.Assert.assertEquals;

import java.net.URISyntaxException;

import org.junit.Test;

import it.cnr.istc.stlab.owlunit.workers.Utils;

public class TestUtils {

	@Test
	public void test1() {
		try {
			String actual = Utils.resolveLocationString("src/main/resources/testResources/testSuiteExecutor1.ttl");
			assertEquals(
					"file:///Users/lgu/workspace/xd/owl-unit/src/main/resources/testResources/testSuiteExecutor1.ttl",
					actual);
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
	}
}
