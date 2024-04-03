package org.metadatacenter.cedar.artifactLib;

import org.metadatacenter.artifacts.model.core.FieldSchemaArtifact;
import org.metadatacenter.artifacts.model.core.builders.FieldSchemaArtifactBuilder;
import org.metadatacenter.cedar.csv.CedarCsvParser;

import java.net.URI;
import java.util.Optional;

public interface FieldGenerator {
  FieldSchemaArtifact generateFieldArtifactSchema(CedarCsvParser.Node node);

  default void buildWithIdentifier(FieldSchemaArtifactBuilder fieldSchemaArtifactBuilder, Optional<String> identifier){
    identifier.ifPresent(fieldSchemaArtifactBuilder::withIdentifier);
  }

  default void buildWithPropertyIri(FieldSchemaArtifactBuilder fieldSchemaArtifactBuilder, Optional<String> propertyIri){
    propertyIri.ifPresent(s -> fieldSchemaArtifactBuilder.withPropertyUri(URI.create(s)));
  }
}