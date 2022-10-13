package org.metadatacenter.cedar.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.stereotype.Component;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2022-10-12
 */
@Component
public class OntologyEntityCollapser {

    public JsonNode collapseOntologyEntityNodes(JsonNode node) {
        if(node instanceof ObjectNode objectNode) {
            if(objectNode.has("@id") && objectNode.has("rdfs:label") && objectNode.size() == 2) {
                var valueFieldValue = objectNode.get("@id");
                if(valueFieldValue != null) {
                    return collapseOntologyEntityNodes(valueFieldValue);
                }
            }
            for(var it = objectNode.fieldNames(); it.hasNext(); ) {
                var fieldName = it.next();
                var fieldValue = objectNode.get(fieldName);
                var collapsedNode = collapseOntologyEntityNodes(fieldValue);
                objectNode.set(fieldName, collapsedNode);
            }
        }
        if(node instanceof ArrayNode arrayNode) {
            for(int i = 0; i < arrayNode.size(); i++) {
                var containedNode = arrayNode.get(i);
                var collapsedNode = collapseOntologyEntityNodes(containedNode);
                arrayNode.set(i, collapsedNode);
            }
        }
        return node;
    }
}