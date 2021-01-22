# OWLunit	

OWLunit is a tool that allows you to run unit tests for ontologies defined according to [OWLUnit Ontology](https://w3id.org/owlunit/ontology). OWLunit is also able too run test cases defined by using [ODP's test annotation schema](http://www.ontologydesignpatterns.org/schemas/testannotationschema.owl) and [TESTaLOD](https://github.com/TESTaLOD/TESTaLOD).

OWLunit runs three kinds of test cases: Competency Question Verification, Inference Verification, Error Provocation Verification.

## Competency Question Verification

The Competency Question (CQ) Verification  consists in testing whether the ontology vocabulary allows to convert a CQ, reflecting an ontology requirement, to a SPARQL query. 
A test case can be specified according to the [OWLUnit Ontology](https://w3id.org/owlunit/ontology) as follows

```
@prefix owlunit: <https://w3id.org/OWLunit/ontology/> .
@prefix ex: <https://raw.githubusercontent.com/luigi-asprino/owl-unit/main/examples/> .
@prefix foaf: <http://xmlns.com/foaf/0.1/> .

ex:cq1.ttl a owlunit:CompetencyQuestionVerification ;
 	owlunit:hasCompetencyQuestion "What are the interests of a certain person?" ;
 	owlunit:hasSPARQLUnitTest "PREFIX foaf: <http://xmlns.com/foaf/0.1/>  SELECT DISTINCT ?interest {?person foaf:interest ?interest}" ;
	owlunit:hasInputData ex:datacq1.ttl ;
	owlunit:hasInputTestDataCategory owlunit:ToyDataset ;
	owlunit:hasExpectedResult "{  \"head\": {  \"vars\": [  \"interest\" ] } ,  \"results\": {  \"bindings\": [ {  \"interest\": {  \"type\":  \"uri\" ,  \"value\":  \"https://raw.githubusercontent.com/luigi-asprino/owl-unit/main/examples/Basketball\" } } ] } }";
	owlunit:testsOntology foaf: .
	
```


OWLunit makes sure that: 1. the IRI used within the SPARQL query are defined in  the tested ontology, 2. (if input data is provided) the result of the the SPARQL unit test query evaluated over the input data is isomorphic to the expected result.
OWLunit is also able to run test cases CQ verification tests defined according to the [ODP's test annotation schema](http://www.ontologydesignpatterns.org/schemas/testannotationschema.owl) and [TESTaLOD ontology](https://github.com/TESTaLOD/TESTaLOD) as follows.


```
@prefix ex: <https://raw.githubusercontent.com/luigi-asprino/owl-unit/main/examples/> .
@prefix foaf: <http://xmlns.com/foaf/0.1/> .
@prefix testannotationschema: <http://www.ontologydesignpatterns.org/schemas/testannotationschema.owl#> .
@prefix testalod: <https://raw.githubusercontent.com/TESTaLOD/TESTaLOD/master/ontology/testalod.owl#> .


ex:cq1-testalod.ttl  testannotationschema:hasCQ "What are the interests of a certain person?" ;
 	testannotationschema:hasSPARQLQueryUnitTest "PREFIX foaf: <http://xmlns.com/foaf/0.1/>  SELECT DISTINCT ?interest {?person foaf:interest ?interest}" ;
	testalod:hasInputTestDataUri  ex:datacq1.ttl  ;
	testalod:hasInputTestDataCategory testalod:ToyDataset ;
	testannotationschema:hasExpectedResult "{  \"head\": {  \"vars\": [  \"interest\" ] } ,  \"results\": {  \"bindings\": [ {  \"interest\": {  \"type\":  \"uri\" ,  \"value\":  \"https://raw.githubusercontent.com/luigi-asprino/owl-unit/main/examples/Basketball\" } } ] } }" .
```

In this case the condition 1. is not evaluated.

## Inference Verification

The Inference Verification focuses on checking the inferences over the ontologies, by comparing the expected inferences to the actual ones.
A test case of this kind can be specified according to the [OWLUnit Ontology](https://w3id.org/owlunit/ontology) as follows

```
@prefix owlunit: <https://w3id.org/OWLunit/ontology/> .
@prefix ex: <https://raw.githubusercontent.com/luigi-asprino/owl-unit/main/examples/> .
@prefix dul: <http://www.ontologydesignpatterns.org/ont/dul/DUL.owl#> .

ex:iv.ttl a owlunit:InferenceVerification ;
	owlunit:hasInputData ex:ivdata.ttl ;
	owlunit:hasSPARQLUnitTest "PREFIX  ex: <https://raw.githubusercontent.com/luigi-asprino/owl-unit/main/examples/>  PREFIX dul: <http://www.ontologydesignpatterns.org/ont/dul/DUL.owl#>  ASK { ex:Luigi a dul:Agent }" ;
	owlunit:hasReasoner owlunit:HermiT ;
	owlunit:hasExpectedResult true ;
 	owlunit:testsOntology dul: .
	
```
OWLunit makes sure that: 1. the tested ontology is consistent, 2. (if input data is provided) ontology and input data together don't lead to any inconsistency, 3. (if a SPARQL unit test is provided) the result of the SPARQL unit test is equivalent to the expected result.

## Error Provocation 

The Error Provocation test is intended to stress the ontology by injecting inconsistent data that violates the ontology.
A test case of this kind can be specified according to the [OWLUnit Ontology](https://w3id.org/owlunit/ontology) as follows

```
@prefix owlunit: <https://w3id.org/OWLunit/ontology/> .
@prefix ex: <https://raw.githubusercontent.com/luigi-asprino/owl-unit/main/examples/> .
@prefix dul: <http://www.ontologydesignpatterns.org/ont/dul/DUL.owl#> .

ex:ep.ttl a owlunit:ErrorProvocation ;
	owlunit:hasInputData ex:epdata.ttl ;
 	owlunit:testsOntology dul: .
```
OWLunit makes sure that ontology and input data are inconsistent together.

