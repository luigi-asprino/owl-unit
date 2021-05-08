package it.cnr.istc.stlab.owlunit.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import org.apache.jena.fuseki.main.FusekiServer;
import org.apache.jena.fuseki.main.FusekiServer.Builder;
import org.apache.jena.query.Dataset;
import org.apache.jena.query.DatasetFactory;
import org.apache.jena.riot.RDFDataMgr;
import org.junit.Test;

import it.cnr.istc.stlab.owlunit.workers.CompetencyQuestionVerificationExecutor;
import it.cnr.istc.stlab.owlunit.workers.OWLUnitException;

public class CQVerificationTest {

	@Test
	public void test1() {
		String URItest = "https://w3id.org/OWLunit/test/test1";
		String fileIn = "src/main/resources/testResources/test1.ttl";
		CompetencyQuestionVerificationExecutor cqve = new CompetencyQuestionVerificationExecutor(URItest);
		cqve.setFileIn(fileIn);
		try {
			assertTrue(cqve.runTest());
		} catch (OWLUnitException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void test2() {
		String URItest = "https://w3id.org/OWLunit/test/test2";
		String fileIn = "src/main/resources/testResources/test2.ttl";
		CompetencyQuestionVerificationExecutor cqve = new CompetencyQuestionVerificationExecutor(URItest);
		cqve.setFileIn(fileIn);
		try {
			assertFalse(cqve.runTest());
		} catch (OWLUnitException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void test3() {
		String URItest = "https://w3id.org/OWLunit/test/test3";
		String fileIn = "src/main/resources/testResources/test3.ttl";
		CompetencyQuestionVerificationExecutor cqve = new CompetencyQuestionVerificationExecutor(URItest);
		cqve.setFileIn(fileIn);
		assertThrows(OWLUnitException.class, () -> {
			cqve.runTest();
		});
	}

	@Test
	public void test4() {
		String URItest = "https://w3id.org/OWLunit/test/test4";
		String fileIn = "src/main/resources/testResources/test4.ttl";
		CompetencyQuestionVerificationExecutor cqve = new CompetencyQuestionVerificationExecutor(URItest);
		cqve.setFileIn(fileIn);
		try {
			assertTrue(cqve.runTest());
		} catch (OWLUnitException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void test5() {
		String URItest = "https://w3id.org/OWLunit/test/test5";
		String fileIn = "src/main/resources/testResources/test5.ttl";
		String dataFileIn = "src/main/resources/testResources/data/test5.ttl";

		CompetencyQuestionVerificationExecutor cqve = new CompetencyQuestionVerificationExecutor(URItest);
		cqve.setFileIn(fileIn);

		Dataset ds = DatasetFactory.create();
		RDFDataMgr.read(ds, dataFileIn);

		Builder builder = FusekiServer.create();
		builder.port(3000);
		FusekiServer server = builder.add("test", ds).build();
		Runnable r = () -> {
			server.start();
		};

		Thread t = new Thread(r);
		t.start();

		try {
			Thread.sleep(1000);
			assertTrue(cqve.runTest());
			server.stop();
			t.join();
		} catch (OWLUnitException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

	}

	@Test
	public void test6() {
		String URItest = "https://w3id.org/OWLunit/test/test6";
		String fileIn = "src/main/resources/testResources/test6.ttl";
		CompetencyQuestionVerificationExecutor cqve = new CompetencyQuestionVerificationExecutor(URItest);
		cqve.setFileIn(fileIn);
		try {
			assertTrue(cqve.runTest());
		} catch (OWLUnitException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void test7() {
		String URItest = "https://w3id.org/OWLunit/test/test9";
		String fileIn = "src/main/resources/testResources/test9.ttl";
		CompetencyQuestionVerificationExecutor cqve = new CompetencyQuestionVerificationExecutor(URItest);
		cqve.setFileIn(fileIn);
		try {
			assertTrue(cqve.runTest());
		} catch (OWLUnitException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void test10() {
		String URItest = "https://w3id.org/OWLunit/test/test10";
		String fileIn = "src/main/resources/testResources/test10.ttl";
		CompetencyQuestionVerificationExecutor cqve = new CompetencyQuestionVerificationExecutor(URItest);
		cqve.setFileIn(fileIn);
		try {
			assertTrue(cqve.runTest());
		} catch (OWLUnitException e) {
			e.printStackTrace();
		}
	}
	
	
	@Test
	public void test11() {
		String URItest = "https://w3id.org/OWLunit/test/test11";
		String fileIn = "src/main/resources/testResources/test11.ttl";
		CompetencyQuestionVerificationExecutor cqve = new CompetencyQuestionVerificationExecutor(URItest);
		cqve.setFileIn(fileIn);
		try {
			assertFalse(cqve.runTest());
		} catch (OWLUnitException e) {
			e.printStackTrace();
		}
	}
}
