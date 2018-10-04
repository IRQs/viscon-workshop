package ch.ipt.viscon.payment.api

import com.fasterxml.jackson.annotation.JsonInclude
import javax.validation.constraints.NotNull

@JsonInclude(JsonInclude.Include.NON_NULL)
class PaymentRequest(
        @field:NotNull val vendor: String,
        @field:NotNull val creditCardNumber: String,
        @field:NotNull val amountPayable: Double?
)