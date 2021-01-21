# OWLunit	

OWLunit is a tool that allows you to run unit tests for ontologies defined according to [OWLUnit Ontology](https://w3id.org/owlunit/ontology). OWLunit is also able too run test cases defined by using [ODP's test annotation schema](http://www.ontologydesignpatterns.org/schemas/testannotationschema.owl) and [TESTaLOD](https://github.com/TESTaLOD/TESTaLOD).

OWLunit runs three kinds of test cases: Competency Question Verification, Inference Verification, Error Provocation Verification.

## Competency Question Verification

The Competency Question (CQ) Verification  consists in testing whether the ontology vocabulary allows to convert a CQ, reflecting an ontology requirement, to a SPARQL query. 
A test case can be specified according to the [OWLUnit Ontology](https://w3id.org/owlunit/ontology) as follows

```
@prefix 
```
