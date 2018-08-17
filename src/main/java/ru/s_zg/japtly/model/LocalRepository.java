package ru.s_zg.japtly.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LocalRepository {
    @JsonProperty("Name")
    private String name;

    @JsonProperty("Comment")
    private String comment;

    @JsonProperty("DefaultDistribution")
    private String defaultDistribution;

    @JsonProperty("DefaultComponent")
    private String defaultComponent;
}
