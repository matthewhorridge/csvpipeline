package org.metadatacenter.cedar.csv;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonEnumDefaultValue;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
import org.metadatacenter.cedar.api.CedarNumberType;
import org.metadatacenter.cedar.api.CedarTemporalType;
import org.metadatacenter.cedar.api.JsonSchemaFormat;
import org.metadatacenter.cedar.api.JsonSchemaInfo;
import org.metadatacenter.cedar.api.CedarInputType;

import java.util.Optional;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2022-07-29
 */
public enum CedarCsvInputType {

    // Ontology terms
    @JsonProperty("checkbox")
    CHECKBOX("checkbox", CedarInputType.CHECKBOX, CedarConstraintsType.ONTOLOGY_TERMS, null, JsonSchemaInfo.CedarFieldValueType.IRI),

    // Ontology terms
    @JsonAlias("radiobutton")
    RADIO("radio", CedarInputType.RADIO, CedarConstraintsType.ONTOLOGY_TERMS, null, JsonSchemaInfo.CedarFieldValueType.IRI),

    BOOLEAN("boolean", CedarInputType.RADIO, CedarConstraintsType.ONTOLOGY_TERMS, null, JsonSchemaInfo.CedarFieldValueType.IRI),

    // Ontology terms
    LIST("list", CedarInputType.LIST, CedarConstraintsType.ONTOLOGY_TERMS, null, JsonSchemaInfo.CedarFieldValueType.IRI),

    TYPEAHEAD("typeadhead", CedarInputType.TEXTFIELD, CedarConstraintsType.ONTOLOGY_TERMS, null, JsonSchemaInfo.CedarFieldValueType.IRI),

    // Ontology terms – Weirdly
    // Also String?!
    @JsonAlias("textbox")
    @JsonEnumDefaultValue
    TEXTFIELD("textfield", CedarInputType.TEXTFIELD, CedarConstraintsType.STRING, null, JsonSchemaInfo.CedarFieldValueType.LITERAL),

    // No additional constraints (Why not min len max len?)
    TEXTAREA("textarea", CedarInputType.TEXTAREA, CedarConstraintsType.STRING, null, JsonSchemaInfo.CedarFieldValueType.LITERAL),

    // Numeric constraints
    NUMERIC("numeric", CedarInputType.NUMERIC, CedarConstraintsType.NUMERIC, null, JsonSchemaInfo.CedarFieldValueType.LITERAL),

    @JsonAlias("int")
    INTEGER("integer", CedarNumberType.INT, CedarConstraintsType.NUMERIC, null, JsonSchemaInfo.CedarFieldValueType.LITERAL),

    LONG("long", CedarNumberType.LONG, CedarConstraintsType.NUMERIC, null, JsonSchemaInfo.CedarFieldValueType.LITERAL),

    DECIMAL("decimal", CedarNumberType.DECIMAL, CedarConstraintsType.NUMERIC, null, JsonSchemaInfo.CedarFieldValueType.LITERAL),

    FLOAT("float", CedarNumberType.FLOAT, CedarConstraintsType.NUMERIC, null, JsonSchemaInfo.CedarFieldValueType.LITERAL),

    DOUBLE("double", CedarNumberType.DOUBLE, CedarConstraintsType.NUMERIC, null, JsonSchemaInfo.CedarFieldValueType.LITERAL),

    // No additional constraints
    PHONE_NUMBER("phone-number", CedarInputType.PHONE_NUMBER, CedarConstraintsType.NONE, null, JsonSchemaInfo.CedarFieldValueType.LITERAL),

    // No additional constraints
    EMAIL("email", CedarInputType.EMAIL, CedarConstraintsType.NONE, JsonSchemaFormat.EMAIL, JsonSchemaInfo.CedarFieldValueType.LITERAL),

    DATE("date", CedarTemporalType.DATE, CedarConstraintsType.TEMPORAL, JsonSchemaFormat.DATE, JsonSchemaInfo.CedarFieldValueType.LITERAL),

    TIME("time", CedarTemporalType.TIME, CedarConstraintsType.TEMPORAL, JsonSchemaFormat.TIME, JsonSchemaInfo.CedarFieldValueType.LITERAL),

    DATE_TIME("datetime", CedarTemporalType.DATE_TIME, CedarConstraintsType.TEMPORAL, JsonSchemaFormat.DATE_TIME,
              JsonSchemaInfo.CedarFieldValueType.LITERAL),

    URL("url", CedarInputType.LINK, CedarConstraintsType.NONE, JsonSchemaFormat.URI, JsonSchemaInfo.CedarFieldValueType.IRI),

    IRI("iri", CedarInputType.LINK, CedarConstraintsType.NONE, JsonSchemaFormat.IRI, JsonSchemaInfo.CedarFieldValueType.IRI);

    private final String name;

    private final CedarInputType cedarInputType;

    private final CedarNumberType cedarNumberType;

    private final CedarTemporalType cedarTemporalType;

    private final CedarConstraintsType cedarConstraintsType;

    private final JsonSchemaFormat jsonSchemaFormat;

    private final JsonSchemaInfo.CedarFieldValueType cedarFieldValueType;

    public static CedarCsvInputType getDefaultInputType() {
        return TEXTFIELD;
    }


    CedarCsvInputType(String name,
                      CedarInputType cedarInputType,
                      CedarConstraintsType cedarConstraintsType,
                      JsonSchemaFormat jsonSchemaFormat, JsonSchemaInfo.CedarFieldValueType cedarFieldValueType) {
        this.name = name;
        this.cedarInputType = cedarInputType;
        this.cedarConstraintsType = cedarConstraintsType;
        this.jsonSchemaFormat = jsonSchemaFormat;
        this.cedarFieldValueType = cedarFieldValueType;
        this.cedarNumberType = null;
        this.cedarTemporalType = null;
    }

    CedarCsvInputType(String name,
                      CedarNumberType numberType,
                      CedarConstraintsType cedarConstraintsType,
                      JsonSchemaFormat jsonSchemaFormat, JsonSchemaInfo.CedarFieldValueType cedarFieldValueType) {
        this.name = name;
        this.cedarConstraintsType = cedarConstraintsType;
        this.jsonSchemaFormat = jsonSchemaFormat;
        this.cedarFieldValueType = cedarFieldValueType;
        this.cedarInputType = CedarInputType.NUMERIC;
        this.cedarNumberType = numberType;
        this.cedarTemporalType = null;
    }

    CedarCsvInputType(String name,
                      CedarTemporalType temporalType,
                      CedarConstraintsType cedarConstraintsType,
                      JsonSchemaFormat jsonSchemaFormat, JsonSchemaInfo.CedarFieldValueType cedarFieldValueType) {
        this.name = name;
        this.cedarConstraintsType = cedarConstraintsType;
        this.jsonSchemaFormat = jsonSchemaFormat;
        this.cedarFieldValueType = cedarFieldValueType;
        this.cedarInputType = CedarInputType.TEMPORAL;
        this.cedarNumberType = null;
        this.cedarTemporalType = temporalType;
    }

    public CedarConstraintsType getConstraintsType() {
        return cedarConstraintsType;
    }

    @JsonValue
    public String getName() {
        return name;
    }

    public CedarInputType getCedarInputType() {
        return cedarInputType;
    }

    public Optional<CedarNumberType> getCedarNumberType() {
        return Optional.ofNullable(cedarNumberType);
    }

    public Optional<CedarTemporalType> getCedarTemporalType() {
        return Optional.ofNullable(cedarTemporalType);
    }

    public Optional<JsonSchemaFormat> getJsonSchemaFormat() {
        return Optional.ofNullable(jsonSchemaFormat);
    }

    public Optional<JsonSchemaInfo.CedarFieldValueType> getJsonSchemaValueType() {
        return Optional.ofNullable(cedarFieldValueType);
    }
}