package ch.ipt.viscon.catalogue

import com.fasterxml.jackson.annotation.JsonProperty
import io.dropwizard.Configuration

public class CatalogueConfiguration() : Configuration() {
    @JsonProperty("template")
    public var template: String=""

    @JsonProperty("defaultName")
    public var defaultName: String="Stranger"
}