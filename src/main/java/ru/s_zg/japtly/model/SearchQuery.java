package ru.s_zg.japtly.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class SearchQuery {
    @JsonProperty
    private String q;

    @JsonProperty
    private Integer withDeps;

    @JsonProperty
    private ResultFormat format;

    public enum ResultFormat {
        @JsonProperty("compact") COMPACT,
        @JsonProperty("details") DETAILS;
    }
}
