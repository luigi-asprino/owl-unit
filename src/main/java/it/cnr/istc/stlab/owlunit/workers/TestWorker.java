package it.cnr.istc.stlab.owlunit.workers;

public interface TestWorker {

	public boolean runTest() throws OWLUnitException;

	public void setFileIn(String fi);
}
