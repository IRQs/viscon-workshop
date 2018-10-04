package ch.ipt.viscon.payment

import com.fasterxml.jackson.annotation.JsonProperty
import io.dropwizard.Configuration

public class PaymentConfiguration() : Configuration() {
    @JsonProperty("template")
    public var template: String=""

    @JsonProperty("defaultName")
    public var defaultName: String="Stranger"
}