package org.metadatacenter.cedar.api;

import com.fasterxml.jackson.annotation.*;

import java.time.Instant;
import java.util.List;
import java.util.Map;

import static com.fasterxml.jackson.annotation.JsonProperty.Access.READ_ONLY;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2022-07-30
 */
@JsonTypeName(SerializableTemplateField.TYPE)
public record SerializableTemplateField(@JsonUnwrapped @JsonProperty(access = READ_ONLY) TemplateFieldJsonSchemaMixin jsonSchemaMixin,
                                        @JsonProperty("schema:schemaVersion") ModelVersion modelVersion,
                                        @JsonUnwrapped @JsonProperty(access = READ_ONLY) CedarTemplateField templateField,
                                        @JsonIgnore JsonSchemaFormat jsonSchemaFormat) implements SerializableTemplateNode {

    static final String TYPE = "https://schema.metadatacenter.org/core/TemplateField";

    public static SerializableTemplateField wrap(CedarTemplateField templateField,
                                                 String jsonSchemaTitle,
                                                 String jsonSchemaDescription) {

        var jsonSchemaType = templateField.ui().inputType().getFixedValueType().orElse(templateField.valueConstraints().getJsonSchemaType());


        var format = templateField.ui().inputType().getJsonSchemaFormat().orElse(null);
        var jsonSchemaInfo = new TemplateFieldJsonSchemaMixin(jsonSchemaTitle,
                                                              jsonSchemaDescription,
                                                              jsonSchemaType,
                                                              format,
                                                              templateField.valueConstraints().isMultipleChoice());
        return new SerializableTemplateField(jsonSchemaInfo,
                                             ModelVersion.V1_6_0,
                                             templateField,
                                             format);
    }

    @JsonProperty("@type")
    public String getType() {
        return TYPE;
    }

    @JsonProperty("@context")
    public Map<String, Object> getContext() {
        return JsonLdInfo.get().getFieldContextBoilerPlate();
    }

    @JsonCreator
    public static CedarTemplateField fromJson(@JsonProperty("@id") CedarId identifier,
                                              @JsonProperty("schema:identifier") String schemaIdentifier,
                                              @JsonProperty("schema:name") String schemaName,
                                              @JsonProperty("schema:description") String schemaDescription,
                                              @JsonProperty("pav:derivedFrom") String pavDerivedFrom,
                                              @JsonProperty("skos:prefLabel") String skosPrefLabel,
                                              @JsonProperty("skos:altLabel") List<String> skosAltLabel,
                                              @JsonProperty("pav:version") String version,
                                              @JsonProperty("bibo:Status") CedarArtifactStatus biboStatus,
                                              @JsonProperty("pav:previousVersion") String previousVersion,
                                              @JsonProperty("_valueConstraints") CedarFieldValueConstraints valueConstraints,
                                              @JsonProperty("_ui") CedarFieldUi ui,@JsonProperty("pav:createdOn") Instant pavCreatedOn,
                                              @JsonProperty("pav:createdBy") String pavCreatedBy,
                                              @JsonProperty("pav:lastUpdatedOn") Instant pavLastUpdatedOn,
                                              @JsonProperty("oslc:modifiedBy") String oslcModifiedBy
                                              ) {
        return new CedarTemplateField(identifier,
                                      new CedarArtifactInfo(
                                              schemaIdentifier,
                                              schemaName,
                                              schemaDescription,
                                              pavDerivedFrom,
                                              skosPrefLabel,
                                              skosAltLabel
                                      ),
                                      new CedarVersionInfo(
                                              previousVersion,
                                              biboStatus,
                                              version
                                      ),
                                      valueConstraints,
                                      ui,
                                      new CedarArtifactModificationInfo(pavCreatedOn,
                                                                        pavCreatedBy,
                                                                        pavLastUpdatedOn,
                                                                        oslcModifiedBy));
    }

    @Override
    public String getSchemaName() {
        return templateField.cedarArtifactInfo().schemaName();
    }
}