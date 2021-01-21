package it.cnr.istc.stlab.owlunit.resources;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class IntegrityConstraintCheck {

	private static final Logger logger = LoggerFactory.getLogger(IntegrityConstraintCheck.class);

//	public Response consistencyCheck(@QueryParam("IRI") String iri) {
//
//		logger.trace("Integrity constraint check " + iri);
//
//		try {
//			IntegrityConstraintCheckWorker iccw = new IntegrityConstraintCheckWorker(iri);
//			Model m;
//			if (iccw.runTest()) {
//				logger.trace("true");
//				m = Utils.getTrue(iri);
//			} else {
//				logger.trace("false");
//				m = Utils.getFalse(iri);
//			}
//			ByteArrayOutputStream bos = new ByteArrayOutputStream();
//			m.write(bos, "RDF/XML");
//			return Response.ok(bos.toString()).build();
//		} catch (OWLUnitException e) {
//			logger.trace("Error "+e.getMessage());
//			e.printStackTrace();
//		}
//		return Response.serverError().build();
//	}

}
