package ch.ipt.viscon.payment.api

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonInclude.Include
import com.fasterxml.jackson.annotation.JsonProperty

@JsonInclude(Include.NON_NULL)
data class PaymentResponse(
        @JsonProperty val processed: Boolean,
        @JsonProperty val amount: Double?,
        @JsonProperty val reason: String? = null
)