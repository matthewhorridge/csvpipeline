package org.metadatacenter.cedar.artifactLib;

import org.metadatacenter.artifacts.model.core.ElementSchemaArtifact;
import org.metadatacenter.cedar.api.CedarId;
import org.metadatacenter.cedar.csv.CedarCsvParser;
import org.metadatacenter.cedar.csv.Identifier;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.util.Optional;
import java.util.UUID;

@Component
public class ElementGenerator {
  private FieldGeneratorFactory fieldGeneratorFactory;

  public ElementGenerator(FieldGeneratorFactory fieldGeneratorFactory) {
    this.fieldGeneratorFactory = fieldGeneratorFactory;
  }

  public ElementSchemaArtifact generateElementSchemaArtifact(CedarCsvParser.Node node, String name, String description) {
    //Name or description could be passed in when build umbrella element
    //Otherwise, both of them are null
    if(description == null){
      description = node.getDescription();
    }
    if(name == null){
      name = node.getSchemaName();
    }
    final String jsonSchemaDescription = name + " element is generated by CEDAR CLI";
    var jsonLdId = CedarId.resolveTemplateElementId(UUID.randomUUID().toString());
    var builder = ElementSchemaArtifact.builder();

    for (var child : node.getChildNodes()) {
      System.out.println(child.getTitle());
      if (child.isElement()) {
        var elementSchemaArtifact = generateElementSchemaArtifact(child, null, null);
        builder.withElementSchema(elementSchemaArtifact);
      } else if (child.isIdentifyElement()) {
        var elementSchemaArtifact = generateIdentifierElement(child, null, null);
        builder.withElementSchema(elementSchemaArtifact);
      } else if (child.isField()) {
        var fieldSchemaArtifact = fieldGeneratorFactory.generateFieldSchemaArtifact(child);
        builder.withFieldSchema(fieldSchemaArtifact);
      }
    }

//    buildWithIdentifier(builder, node.getFieldIdentifier());
    buildWithPropertyIri(builder, node.getPropertyIri());

    return builder
        .withName(name)
        .withPreferredLabel(node.getTitle())
        .withDescription(description)
        .withJsonSchemaDescription(jsonSchemaDescription)
        .withIsMultiple(node.isMultiValued())
        .withJsonLdId(URI.create(jsonLdId.value()))
        .build();
  }

  public ElementSchemaArtifact generateIdentifierElement(CedarCsvParser.Node node, String elementName, String description) {
    if(description == null){
      description = node.getDescription();
    }
    if(elementName == null){
      elementName = node.getIdentifierSchemaName(Identifier.IDENTIFIER_ELEMENT);
    }

    final String jsonSchemaDescription = elementName + " element is generated by CEDAR CLI";
    var jsonLdId = CedarId.resolveTemplateElementId(UUID.randomUUID().toString());
    var builder = ElementSchemaArtifact.builder();

    var textFieldGenerator = new TextFiledGenerator();
    var controlledTermFieldGenerator = new ControlledTermFieldGenerator();
    for (var child : node.getChildNodes()) {
      //should be only one child: identifier
      var identifierArtifact = textFieldGenerator.generateIdentifierFieldArtifactSchema(child);
      builder.withFieldSchema(identifierArtifact);
    }

    //build identifier scheme field
    var identifierSchemeArtifact = controlledTermFieldGenerator.generateIdentifierSchemeFieldArtifactSchema(node);
    builder.withFieldSchema(identifierSchemeArtifact);

//    buildWithIdentifier(builder, node.getFieldIdentifier());
    buildWithIdentifierElementPropertyIri(builder, node.getPropertyIri(), elementName);

    return builder
        .withName(elementName)
        .withPreferredLabel(node.getIdentifierTitle(Identifier.IDENTIFIER_ELEMENT))
        .withDescription(description)
        .withJsonSchemaDescription(jsonSchemaDescription)
        .withIsMultiple(node.isMultiValued())
        .withJsonLdId(URI.create(jsonLdId.value()))
        .build();
  }

  private void buildWithIdentifier(ElementSchemaArtifact.Builder builder, Optional<String> identifier){
    identifier.ifPresent(builder::withSchemaOrgIdentifier);
  }

  private void buildWithPropertyIri(ElementSchemaArtifact.Builder builder, Optional<String> propertyIri){
    propertyIri.ifPresent(s -> builder.withPropertyUri(URI.create(s)));
  }

  private void buildWithIdentifierElementPropertyIri(ElementSchemaArtifact.Builder builder, Optional<String> propertyIri, String elementPropertyName){
    propertyIri.ifPresent(s -> {
      URI uri = URI.create(s);
      String path = uri.getPath();
      String newPath = path.substring(0, path.lastIndexOf('/') + 1) + elementPropertyName;
      URI newUri = uri.resolve(newPath);
      builder.withPropertyUri(newUri);
    });
  }
}
