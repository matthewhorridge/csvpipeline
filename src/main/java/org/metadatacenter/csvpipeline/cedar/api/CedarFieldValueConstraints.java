package org.metadatacenter.csvpipeline.cedar.api;

import com.fasterxml.jackson.annotation.*;
import org.metadatacenter.csvpipeline.cedar.csv.Cardinality;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2022-07-26
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.DEDUCTION, defaultImpl = CedarStringValueConstraints.class)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonSubTypes({
        @JsonSubTypes.Type(CedarStringValueConstraints.class),
        @JsonSubTypes.Type(CedarNumericValueConstraints.class),
        @JsonSubTypes.Type(CedarTemporalValueConstraints.class),
        @JsonSubTypes.Type(EnumerationValueConstraints.class)
})
public interface CedarFieldValueConstraints {

    @JsonIgnore()
    Required requiredValue();

    @JsonProperty("requiredValue")
    default boolean isRequiredValue() {
        return requiredValue() == Required.REQUIRED;
    }

    @JsonIgnore
    Cardinality cardinality();

    @JsonProperty("multipleChoice")
    default boolean isMultipleChoice() {
        return cardinality() == Cardinality.MULTIPLE;
    }

    static CedarFieldValueConstraints empty() {
        return new CedarFieldValueConstraints() {
            @Override
            public Required requiredValue() {
                return Required.OPTIONAL;
            }

            @Override
            public Cardinality cardinality() {
                return Cardinality.SINGLE;
            }
        };
    }

}
