package ch.ipt.viscon.order.resources;

import ch.ipt.viscon.order.OrderConfiguration;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

@Path("/info")
public class InfoResource {

    private OrderConfiguration conf;

    public InfoResource(OrderConfiguration conf) {
        this.conf = conf;
    }

    @GET
    public String getShopName() {
        return conf.getShopName() + System.lineSeparator();
    }
}
