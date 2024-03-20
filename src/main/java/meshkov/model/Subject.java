package meshkov.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum Subject {
    @JsonProperty("English")
    ENGLISH,
    @JsonProperty("Math")
    MATH,
    @JsonProperty("History")
    HISTORY,
    @JsonProperty("Economics")
    ECONOMICS,
    @JsonProperty("Programming")
    PROGRAMMING,
    @JsonProperty("Philosophy")
    PHILOSOPHY,
    @JsonProperty("Sociology")
    SOCIOLOGY,
    @JsonProperty("Physics")
    PHYSICS
}
