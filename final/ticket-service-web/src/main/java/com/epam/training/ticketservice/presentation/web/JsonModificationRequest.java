package com.epam.training.ticketservice.presentation.web;

import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.Data;

@Data
public class JsonModificationRequest {
    String entity;

    ObjectNode properties = new ObjectNode(JsonNodeFactory.instance); //TODO is there some way to do this that doesnt pull in jackson databind?

    @JsonAnySetter
    public void add(String key, JsonNode value) {
        properties.set(key, value);
    }
}
