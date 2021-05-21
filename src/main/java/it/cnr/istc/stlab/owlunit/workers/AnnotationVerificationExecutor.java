package it.cnr.istc.stlab.owlunit.workers;

import java.util.ArrayList;
import java.util.List;

import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.riot.RDFDataMgr;
import org.topbraid.shacl.validation.ValidationUtil;

import it.cnr.istc.stlab.owlunit.Constants;

public class AnnotationVerificationExecutor extends TestWorkerBase {

	public static final String DEFAULT_SHAPES = "shapes/ontology.ttl";

	public AnnotationVerificationExecutor(String testCaseIRI) {
		super();
		super.testCaseIRI = testCaseIRI;
	}

	private List<String> getHasShapes() throws OWLUnitException {

		List<String> result = new ArrayList<>();

		super.model.listStatements(null, super.model.getProperty(Constants.OWLUNIT_ONTOLOGY_PREFIX + "hasShapes"),
				(RDFNode) null).forEach(s -> {
					result.add(s.getObject().asResource().getURI());
				});

		if (result.isEmpty()) {
			result.add(DEFAULT_SHAPES);
		}

		return result;
	}

	@Override
	public boolean runTest() throws OWLUnitException {

		loadTest();

		Model testedOntology = getTestedOntologyAsModel();
		Model shapes = ModelFactory.createDefaultModel();

		for (String shapesFiles : getHasShapes()) {
			RDFDataMgr.read(shapes, shapesFiles);
		}

//		testedOntology.write(System.out);

//		System.out.println(testedOntology.getNsPrefixMap());

		String basePrefix = testedOntology.getNsPrefixMap().get("");

		Resource validationResult = ValidationUtil.validateModel(testedOntology, shapes, false);
		Model reportModel = validationResult.getModel();

//		validationResult.getModel().write(System.out, "TTL");

		String q = "PREFIX sh: <http://www.w3.org/ns/shacl#> SELECT DISTINCT ?node ?message ?severity { "
				+ " ?vr a sh:ValidationResult . " + " ?vr sh:focusNode ?node . " + " ?vr sh:resultSeverity ?severity ."
				+ " ?vr sh:resultMessage ?message . }";
		QueryExecution qexec = QueryExecutionFactory.create(q, reportModel);

		ResultSet rs = qexec.execSelect();
		boolean conforms = true;
		System.out.println("Report");
		while (rs.hasNext()) {
			QuerySolution qs = (QuerySolution) rs.next();

			String targetNode = qs.get("node").asResource().getURI();
			String message = qs.get("message").asLiteral().getValue().toString();
			String severity = qs.get("severity").asResource().getLocalName();
			if (targetNode.startsWith(basePrefix)) {
				System.out.println(String.format("%s:[target: %s] %s", severity, targetNode, message));
				if (severity.equals("Violation")) {
					conforms = false;
				}
			}

		}
		return conforms;
	}

}
