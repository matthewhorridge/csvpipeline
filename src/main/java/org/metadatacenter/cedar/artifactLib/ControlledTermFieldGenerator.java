package org.metadatacenter.cedar.artifactLib;

import org.metadatacenter.artifacts.model.core.FieldSchemaArtifact;
import org.metadatacenter.artifacts.model.core.builders.ControlledTermFieldBuilder;
import org.metadatacenter.artifacts.model.core.fields.constraints.ValueType;
import org.metadatacenter.cedar.api.constraints.EnumerationValueConstraints;
import org.metadatacenter.cedar.csv.CedarCsvParser;

import java.net.URI;
import java.util.Optional;

public class ControlledTermFieldGenerator implements FieldGenerator {
  @Override
  public FieldSchemaArtifact generateFieldArtifactSchema(CedarCsvParser.Node node) {
    var builder = FieldSchemaArtifact.controlledTermFieldBuilder();
    var constraints = node.getOntologyTermsConstraints();
    updateWithOntologies(builder, constraints);
    buildWithIdentifier(builder, node.getFieldIdentifier());
    buildWithPropertyIri(builder, node.getPropertyIri());
    //TODO default value?
    return builder
        .withIsMultiple(node.isMultiValued())
        .withRequiredValue(node.isRequired())
        .withName(node.getSchemaName())
        .withDescription(node.getDescription())
        .withHidden(node.isHidden())
        .build();
  }

  private void updateWithOntologies(ControlledTermFieldBuilder builder, Optional<EnumerationValueConstraints> constraints){
    if(constraints.isPresent()){
      var branches = constraints.get().branches();
      var ontologies = constraints.get().ontologies();
      var classes = constraints.get().classes();

      for (var branch : branches) {
        //build with branch uri, source, acronym, name, maxDepth
        builder.withBranchValueConstraint(URI.create(branch.uri()), branch.source(), branch.acronym(), branch.name(), branch.maxDepth());
      }

      for (var ontology : ontologies) {
        //build with ontology uri,  acronym, and name
        builder.withOntologyValueConstraint(URI.create(ontology.uri()), ontology.acronym(), ontology.name());
      }

      for (var c : classes) {
        //build with class uri, source, label, prefLabel, type
        builder.withClassValueConstraint(URI.create(c.iri()), c.source(), c.label(), c.getPrefLabel(), ValueType.ONTOLOGY_CLASS);
      }
    }
  }
}