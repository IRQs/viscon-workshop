package ch.ipt.viscon.payment.resources

import ch.ipt.viscon.payment.api.PaymentRequest
import ch.ipt.viscon.payment.api.PaymentResponse
import org.slf4j.LoggerFactory
import javax.validation.Valid
import javax.validation.constraints.NotNull
import javax.ws.rs.*
import javax.ws.rs.core.MediaType

@Path("/payment")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
class PaymentResource {

    companion object {
        val log = LoggerFactory.getLogger(PaymentResource::class.java)
    }

    @POST
    fun processPayment(@NotNull @Valid paymentRequest: PaymentRequest): PaymentResponse {

        log.info("Processing payment of CHF ${paymentRequest.amountPayable} with credit card ${paymentRequest.creditCardNumber} issued by vendor ${paymentRequest.vendor}")

        // Pseudo-validation: Only accept credit cards that start with 9999
        return if (paymentRequest.creditCardNumber.startsWith("9999")) {
            PaymentResponse(true, paymentRequest.amountPayable)
        } else {
            PaymentResponse(false, paymentRequest.amountPayable, "Credit Card was declined")
        }
    }

}