# OWLunit

OWLunit allows you to run unit tests for ontologies defined according to the [OWLUnit Ontology](https://w3id.org/OWLunit/ontology/0.0.2). OWLunit is also able too run test cases defined by using [ODP's test annotation schema](http://www.ontologydesignpatterns.org/schemas/testannotationschema.owl) and [TESTaLOD](https://github.com/TESTaLOD/TESTaLOD).

OWLunit runs three kinds of test cases: Competency Question Verification, Inference Verification, Error Provocation Verification.

### Annotation Verification

Annotation verification tests aim at assessing the annotations (e.g. labels, comments) associated with ontology entities.

A test case of this kind can be specified according to the [OWLUnit Ontology](https://w3id.org/owlunit/ontology) as follows

```
@prefix owlunit: <https://w3id.org/OWLunit/ontology/> .
@prefix ex: <https://w3id.org/OWLunit/examples/> .

ex:annotationverification.ttl a owlunit:AnnotationVerification ;
	owlunit:testsOntology owlunit: .
```

OWLUnit verifies that the tested ontology complies with its [default ontology shape](https://raw.githubusercontent.com/luigi-asprino/owl-unit/main/shapes/ontology.ttl).
Alternatively you can define your own shape and specify it by using the ``owlunit:hasShapes`` property.

```
@prefix owlunit: <https://w3id.org/OWLunit/ontology/> .
@prefix ex: <https://w3id.org/OWLunit/examples/> .

ex:annotationverification.ttl a owlunit:AnnotationVerification ;
	owlunit:hasShapes <https://raw.githubusercontent.com/luigi-asprino/owl-unit/main/shapes/ontology.ttl> ; 
	owlunit:testsOntology owlunit: .
```

### Competency Question Verification

The Competency Question (CQ) Verification  consists in testing whether the ontology vocabulary allows to convert a CQ, reflecting an ontology requirement, to a SPARQL query.
A test case can be specified according to the [OWLUnit Ontology](https://w3id.org/owlunit/ontology) as follows

```
@prefix owlunit: <https://w3id.org/OWLunit/ontology/> .
@prefix ex: <https://w3id.org/OWLunit/examples/> .
@prefix foaf: <http://xmlns.com/foaf/0.1/> .

ex:cq1.ttl a owlunit:CompetencyQuestionVerification ;
 	owlunit:hasCompetencyQuestion "What are the interests of a certain person?" ;
 	owlunit:hasSPARQLUnitTest "PREFIX foaf: <http://xmlns.com/foaf/0.1/>  SELECT DISTINCT ?interest {?person foaf:interest ?interest}" ;
	owlunit:hasInputData ex:datacq1.ttl ;
	owlunit:hasInputTestDataCategory owlunit:ToyDataset ;
	owlunit:hasExpectedResult "{  \"head\": {  \"vars\": [  \"interest\" ] } ,  \"results\": {  \"bindings\": [ {  \"interest\": {  \"type\":  \"uri\" ,  \"value\":  \"https://w3id.org/OWLunit/examples/Basketball\" } } ] } }";
	owlunit:testsOntology foaf: .

```


OWLunit makes sure that: 1. the IRIs used within the SPARQL query are defined either in the tested ontology or in the input test data (if provided), the IRIs that don't meet this condition are printed in console; 2. (if input data is provided) the result of the the SPARQL unit test query evaluated over the input data is isomorphic to the expected result. The expected result can be specified either as a JSON serialization (see [example above](https://w3id.org/OWLunit/examples/cq1.ttl)) of the result set of the query or according to the [this vocabulary](https://www.w3.org/2001/sw/DataAccess/tests/result-set#) (see [example below](https://w3id.org/OWLunit/examples/cq1_resultset.ttl)).


```
@prefix owlunit: <https://w3id.org/OWLunit/ontology/> .
@prefix rs:   <http://www.w3.org/2001/sw/DataAccess/tests/result-set#> .
@prefix ex: <https://w3id.org/OWLunit/examples/> .
@prefix foaf: <http://xmlns.com/foaf/0.1/> .
@prefix xsd:   <http://www.w3.org/2001/XMLSchema#> .

ex:cq1_resultset.ttl a owlunit:CompetencyQuestionVerification ;
 	owlunit:hasCompetencyQuestion "What are the interests of a certain person?" ;
 	owlunit:hasSPARQLUnitTest "PREFIX foaf: <http://xmlns.com/foaf/0.1/>  SELECT DISTINCT ?interest {?person foaf:interest ?interest}" ;
	owlunit:hasInputData ex:datacq1.ttl ;
	owlunit:hasInputTestDataCategory owlunit:ToyDataset ;
	owlunit:hasExpectedResult [ a                  rs:ResultSet ;
					  rs:resultVariable  "interest" ;
					  rs:size            "1"^^xsd:int ;
					  rs:solution        [ rs:binding  [ rs:value     <https://w3id.org/OWLunit/examples/Basketball> ;
									     rs:variable  "interest"
									   ]
							     ]
					] ;
	owlunit:testsOntology foaf: .

```

Moreover, you can also test multiple ontologies at a time.

```
@prefix owlunit: <https://w3id.org/OWLunit/ontology/> .
@prefix ex: <https://w3id.org/OWLunit/examples/> .
@prefix foaf: <http://xmlns.com/foaf/0.1/> .

ex:cqmultiple.ttl a owlunit:CompetencyQuestionVerification ;
	owlunit:hasCompetencyQuestion "What are the interests of a certain person?" ;
	owlunit:hasSPARQLUnitTest "PREFIX foaf: <http://xmlns.com/foaf/0.1/> PREFIX DUL: <http://www.ontologydesignpatterns.org/ont/dul/DUL.owl#>  SELECT DISTINCT ?interest {?person foaf:interest ?interest.  ?person a DUL:Person}" ;
	owlunit:testsOntology foaf:, <http://www.ontologydesignpatterns.org/ont/dul/DUL.owl#> .
```


OWLunit is also able to run test cases CQ verification tests defined according to the [ODP's test annotation schema](http://www.ontologydesignpatterns.org/schemas/testannotationschema.owl) and [TESTaLOD ontology](https://github.com/TESTaLOD/TESTaLOD) as follows.


```
@prefix ex: <https://w3id.org/OWLunit/examples/> .
@prefix foaf: <http://xmlns.com/foaf/0.1/> .
@prefix testannotationschema: <http://www.ontologydesignpatterns.org/schemas/testannotationschema.owl#> .
@prefix testalod: <https://raw.githubusercontent.com/TESTaLOD/TESTaLOD/master/ontology/testalod.owl#> .


ex:cq1-testalod.ttl  testannotationschema:hasCQ "What are the interests of a certain person?" ;
 	testannotationschema:hasSPARQLQueryUnitTest "PREFIX foaf: <http://xmlns.com/foaf/0.1/>  SELECT DISTINCT ?interest {?person foaf:interest ?interest}" ;
	testalod:hasInputTestDataUri  ex:datacq1.ttl  ;
	testalod:hasInputTestDataCategory testalod:ToyDataset ;
	testannotationschema:hasExpectedResult "{  \"head\": {  \"vars\": [  \"interest\" ] } ,  \"results\": {  \"bindings\": [ {  \"interest\": {  \"type\":  \"uri\" ,  \"value\":  \"https://w3id.org/OWLunit/examples/Basketball\" } } ] } }" .
```

In this case the condition 1. is not evaluated.

### Inference Verification

The Inference Verification focuses on checking the inferences over the ontologies, by comparing the expected inferences to the actual ones.
A test case of this kind can be specified according to the [OWLUnit Ontology](https://w3id.org/owlunit/ontology) as follows

```
@prefix owlunit: <https://w3id.org/OWLunit/ontology/> .
@prefix ex: <https://w3id.org/OWLunit/examples/> .
@prefix dul: <http://www.ontologydesignpatterns.org/ont/dul/DUL.owl#> .

ex:iv.ttl a owlunit:InferenceVerification ;
	owlunit:hasInputData ex:ivdata.ttl ;
	owlunit:hasSPARQLUnitTest "PREFIX  ex: <https://w3id.org/OWLunit/examples/>  PREFIX dul: <http://www.ontologydesignpatterns.org/ont/dul/DUL.owl#>  ASK { ex:Luigi a dul:Agent }" ;
	owlunit:hasReasoner owlunit:HermiT ;
	owlunit:hasExpectedResult true ;
 	owlunit:testsOntology dul: .

```
OWLunit makes sure that: 1. the tested ontology is consistent, 2. (if input data is provided) ontology and input data together don't lead to any inconsistency, 3. (if a SPARQL unit test is provided) the result of the SPARQL unit test is equivalent to the expected result.

### Error Provocation

The Error Provocation test is intended to stress the ontology by injecting inconsistent data that violates the ontology.
A test case of this kind can be specified according to the [OWLUnit Ontology](https://w3id.org/owlunit/ontology) as follows

```
@prefix owlunit: <https://w3id.org/OWLunit/ontology/> .
@prefix ex: <https://w3id.org/OWLunit/examples/> .
@prefix dul: <http://www.ontologydesignpatterns.org/ont/dul/DUL.owl#> .

ex:ep.ttl a owlunit:ErrorProvocation ;
	owlunit:hasInputData ex:epdata.ttl ;
 	owlunit:testsOntology dul: .
```
OWLunit makes sure that ontology and input data are inconsistent together.


### Test Suite

OWLunit is able to run test cases grouped in test suites.

```
@prefix owlunit: <https://w3id.org/OWLunit/ontology/> .
@prefix ex: <https://w3id.org/OWLunit/examples/> .

ex:suite1.ttl a owlunit:TestSuite ;
	owlunit:hasTestCase ex:cq1-testalod.ttl ;
	owlunit:hasTestCase ex:iv.ttl ;
	owlunit:hasTestCase ex:ep.ttl  .

ex:cq1-testalod.ttl  a owlunit:CompetencyQuestionVerification .
ex:iv.ttl a owlunit:InferenceVerification .
ex:ep.ttl  a owlunit:ErrorProvocation .

```

## Download and Usage

An executable JAR can be obtained from the [Releases](https://github.com/luigi-asprino/owl-unit/releases) page.

The jar can be executed as follows:

```
usage: java -jar OWLUnit-0.0.4.jar [ARGS]
 -c,--test-case <URI>    The URI of the test case to execute.
 -s,--test-suite <URI>   The URI of the test suite to execute.
```

## License

OWLunit is distributed under [Apache 2.0 License](LICENSE)
