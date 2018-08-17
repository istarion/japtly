package ru.s_zg.japtly.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DebPackage {
    @JsonProperty("Architecture")
    private String architecture;

    @JsonProperty("Description")
    private String description;

    @JsonProperty("Filename")
    private String filename;

    @JsonProperty("FilesHash")
    private String filesHash;

    @JsonProperty("Homepage")
    private String homepage;

    @JsonProperty("Installed-Size")
    private String installedSize;

    @JsonProperty("Key")
    private String key;

    @JsonProperty("License")
    private String license;

    @JsonProperty("MD5Sum")
    private String md5sum;

    @JsonProperty("Maintainer")
    private String maintainer;

    @JsonProperty("Package")
    private String pkg;

    @JsonProperty("Priority")
    private String priority;

    @JsonProperty("Recommends")
    private String recommends;

    @JsonProperty("SHA1")
    private String sha1;

    @JsonProperty("SHA256")
    private String sha256;

    @JsonProperty("Section")
    private String section;

    @JsonProperty("ShortKey")
    private String shortKey;

    @JsonProperty("Size")
    private String size;

    @JsonProperty("Vendor")
    private String vendor;

    @JsonProperty("Version")
    private String version;
}
