package org.metadatacenter.cedar.io;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import org.metadatacenter.cedar.api.Multiplicity;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2022-08-02
 *
 * Serialization structure for template fields that are embedded in elements or templates.  Embedded template fields
 * have extra information specified for them such as multiplicity`
 */
public record SerializableEmbeddedTemplateField(@JsonUnwrapped Multiplicity multiplicity,
                                                @JsonProperty("hidden") boolean hidden,
                                                @JsonUnwrapped SerializableTemplateElement templateElement) {

}