package org.metadatacenter.cedar.artifactLib;

import org.metadatacenter.artifacts.model.core.FieldSchemaArtifact;
import org.metadatacenter.artifacts.model.core.builders.LinkFieldBuilder;
import org.metadatacenter.cedar.csv.CedarCsvParser;

import java.net.URI;
import java.util.Optional;

public class LinkFieldGenerator implements FieldGenerator {

  @Override
  public FieldSchemaArtifact generateFieldArtifactSchema(CedarCsvParser.Node node) {
    var builder = FieldSchemaArtifact.linkFieldBuilder();
//    buildWithIdentifier(builder, node.getFieldIdentifier());
    buildWithPropertyIri(builder, node.getPropertyIri());

    return builder
        .withIsMultiple(node.isMultiValued())
        .withRequiredValue(node.isRequired())
        .withName(node.getSchemaName())
        .withDescription(node.getDescription())
        .withJsonSchemaDescription(getJsonSchemaDescription(node))
        .withHidden(node.getRow().visibility().isHidden())
        .withDefaultValue(URI.create(node.getRow().getDefaultValue().getLabel()))
        .build();
  }
}
