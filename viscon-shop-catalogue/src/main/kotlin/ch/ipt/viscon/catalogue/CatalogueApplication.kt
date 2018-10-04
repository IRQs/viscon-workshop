package ch.ipt.viscon.catalogue

import ch.ipt.viscon.catalogue.health.TemplateHealthCheck
import ch.ipt.viscon.catalogue.resources.CatalogueResource
import io.dropwizard.Application
import io.dropwizard.setup.Bootstrap
import io.dropwizard.setup.Environment


class CatalogueApplication() : Application<CatalogueConfiguration>() {
    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            CatalogueApplication().run(*args)
        }
    }

    override fun initialize(bootstrap: Bootstrap<CatalogueConfiguration>) {
        // Don't do anything
    }

    override fun run(config: CatalogueConfiguration, env: Environment) {
        val catalogueResource = CatalogueResource()
        val healthCheck = TemplateHealthCheck(config.template)

        env.healthChecks().register("template", healthCheck)
        env.jersey().register(catalogueResource)
    }

}