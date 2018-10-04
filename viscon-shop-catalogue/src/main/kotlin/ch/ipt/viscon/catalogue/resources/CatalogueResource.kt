package ch.ipt.viscon.catalogue.resources

import ch.ipt.viscon.catalogue.api.Item
import java.util.concurrent.ThreadLocalRandom
import javax.ws.rs.GET
import javax.ws.rs.Path
import javax.ws.rs.Produces
import javax.ws.rs.core.MediaType

@Path("/catalogue")
@Produces(MediaType.APPLICATION_JSON)
class CatalogueResource {

    private fun randomPrice(): Double {

        val randomFactor = ThreadLocalRandom.current().nextInt(1, 10)
        return 1 + 0.5 * randomFactor

    }

    private val items = listOf<Item>(
            Item(1, "Red Bull", randomPrice()),
            Item(2, "Monster Energy", randomPrice()),
            Item(3, "Rockstar Energy", randomPrice()),
            Item(4, "Trojka Energy", randomPrice()),
            Item(5, "ok Energy Drink", randomPrice()),
            Item(6, "M Budget Energy Drink", randomPrice()),
            Item(8, "Prix Garantie Energy Drink", randomPrice()),
            Item(9, "Flying Power Energy Drink", randomPrice()),
            Item(10, "Golden Eagle Energy Drink", randomPrice())
    )

    @GET
    fun getCatalogue(): List<Item> {
        return items
    }


}