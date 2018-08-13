package ru.s_zg.japtly.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class Version {
    @JsonProperty("Version")
    private String version;
}
