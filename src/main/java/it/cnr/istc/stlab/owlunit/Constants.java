package it.cnr.istc.stlab.owlunit;

public class Constants {

	public static final String OWLUNIT_ONTOLOGY_PREFIX = "https://w3id.org/OWLunit/ontology/";
	public static final String TESTALOD_ONTOLOGY_PREFIX = "https://w3id.org/arco/testalod/ontology/";
	public static final String TESTALOD_ONTOLOGY_OLD_PREFIX = "https://raw.githubusercontent.com/TESTaLOD/TESTaLOD/master/ontology/testalod.owl#";
	public static final String TESTANNOTATIONSCCHEMA_PREFIX = "http://www.ontologydesignpatterns.org/schemas/testannotationschema.owl#";
	public static final String TESTANNOTATIONSCHEMA_HASSPARQLQUERYUNITTEST = TESTANNOTATIONSCCHEMA_PREFIX
			+ "hasSPARQLQueryUnitTest";
	public static final String TESTANNOTATIONSCHEMA_HASEXPECTEDRESULT = TESTANNOTATIONSCCHEMA_PREFIX
			+ "hasExpectedResult";
	public static final String OWLUNIT_HASSPARQLQUERYUNITTEST = OWLUNIT_ONTOLOGY_PREFIX + "hasSPARQLUnitTest";

	public static final String SPARQLENDPOINT_DATACATEGORY_OLD = TESTALOD_ONTOLOGY_OLD_PREFIX + "SPARQLendpoint";
	public static final String SPARQLENDPOINT_DATACATEGORY = OWLUNIT_ONTOLOGY_PREFIX + "SPARQLEndpoint";
	public static final String SPARQLENDPOINT_DATACATEGORY_TESTALOD = TESTALOD_ONTOLOGY_PREFIX + "SPARQLEndpoint";

	public static final String TOY_DATASET_DATACATEGORY_OLD = TESTALOD_ONTOLOGY_OLD_PREFIX + "ToyDataset";
	public static final String TOY_DATASET_DATACATEGORY = OWLUNIT_ONTOLOGY_PREFIX + "ToyDataset";
	public static final String TOY_DATASET_DATACATEGORY_TESTALOD = TESTALOD_ONTOLOGY_PREFIX + "ToyDataset";
	public static final String HASTESTCASE = OWLUNIT_ONTOLOGY_PREFIX + "hasTestCase";
	public static final String CQVERIFICATION = OWLUNIT_ONTOLOGY_PREFIX + "CompetencyQuestionVerification";
	public static final String INFERENCEVERIFICATION = OWLUNIT_ONTOLOGY_PREFIX + "InferenceVerification";
	public static final String ERRORPROVOCATION = OWLUNIT_ONTOLOGY_PREFIX + "ErrorProvocation";

}
