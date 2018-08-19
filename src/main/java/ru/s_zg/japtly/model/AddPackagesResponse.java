package ru.s_zg.japtly.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class AddPackagesResponse {
    @JsonProperty("FailedFiles")
    private List<String> failedFiles;

    @JsonProperty("Report")
    private Report report;

    @Data
    public static class Report {
        @JsonProperty("Warnings")
        private List<String> warnings;

        @JsonProperty("Added")
        private List<String> added;

        @JsonProperty("Removed")
        private List<String> removed;
    }
}
