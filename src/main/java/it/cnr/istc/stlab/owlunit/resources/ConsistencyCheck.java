package it.cnr.istc.stlab.owlunit.resources;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ConsistencyCheck {

	private static final Logger logger = LoggerFactory.getLogger(ConsistencyCheck.class);

//	public Response consistencyCheck(@QueryParam("IRI") String iri) {
//
//		logger.trace("Checking consistency of " + iri);
//
//		Model m;
//		try {
//			if (Utils.checkConsistency(iri)) {
//				m = Utils.getTrue(iri);
//			} else {
//				m = Utils.getFalse(iri);
//			}
//			ByteArrayOutputStream bos = new ByteArrayOutputStream();
//			m.write(bos, "RDF/XML");
//			return Response.ok(bos.toString()).build();
//		} catch (OWLOntologyCreationException e) {
//			e.printStackTrace();
//		}
//		return Response.serverError().build();
//	}

}
