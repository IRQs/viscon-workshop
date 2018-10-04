package ch.ipt.viscon.payment

import ch.ipt.viscon.payment.health.TemplateHealthCheck
import ch.ipt.viscon.payment.resources.PaymentResource
import com.fasterxml.jackson.module.kotlin.KotlinModule
import io.dropwizard.Application
import io.dropwizard.setup.Bootstrap
import io.dropwizard.setup.Environment


class PaymentApplication() : Application<PaymentConfiguration>() {
    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            PaymentApplication().run(*args)
        }
    }

    override fun initialize(bootstrap: Bootstrap<PaymentConfiguration>) {
        bootstrap.objectMapper.registerModule(KotlinModule())
    }

    override fun run(config: PaymentConfiguration, env: Environment) {
        val paymentResource = PaymentResource()
        val healthCheck = TemplateHealthCheck(config.template)

        env.healthChecks().register("template", healthCheck)
        env.jersey().register(paymentResource)
    }

}