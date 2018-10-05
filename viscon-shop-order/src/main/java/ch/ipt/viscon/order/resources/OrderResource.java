package ch.ipt.viscon.order.resources;

import ch.ipt.viscon.order.OrderConfiguration;
import ch.ipt.viscon.order.api.*;
import com.orbitz.consul.Consul;
import com.orbitz.consul.HealthClient;
import com.orbitz.consul.model.health.ServiceHealth;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.InternalServerErrorException;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Path("/order")
public class OrderResource {

    private static final Logger LOGGER = LoggerFactory.getLogger(OrderResource.class);

    private Client client;
    private OrderConfiguration conf;
    private Consul consulClient;

    public OrderResource(OrderConfiguration conf, Client client) {
        this.client = client;
        this.conf = conf;
    }

    public OrderResource(OrderConfiguration conf, Client client, Consul consulClient) {
        this(conf, client);
        this.consulClient = consulClient;
    }

    @POST
    public Response orderEnergyDrinks(@NotNull @Valid OrderRequest request) {

        // Perform fake credit card lookup
        String ccNumber = fakeCreditCardLookup(request.getUser());
        LOGGER.info("New order from user {} with credit card number {}", request.getUser(), ccNumber);

        // Retrieve catalogue list
        Response response;
        try {

            if (conf.isServiceDiscoveryEnabled()) {
                HealthClient healthClient = consulClient.healthClient();

                // TODO Step 4: Use Service Discovery with Consul to retrieve the dynamic IP address of the Catalogue service and call its /catalogue operation
                return Response.status(Response.Status.SERVICE_UNAVAILABLE).entity("No healthy Catalogue service found.").build();
            } else {
                response = client.target("http://"+conf.getCatalogueServiceIp()+":9595")
                        .path("catalogue")
                        .request(MediaType.APPLICATION_JSON_TYPE)
                        .get();

            }
        } catch (Exception e) {
            LOGGER.error("Unable to reach Catalogue Service, check error log for stack trace.");
            e.printStackTrace();
            return Response.status(Response.Status.SERVICE_UNAVAILABLE).entity("Unable to reach Catalogue Service. Check log for details.").build();
        }

        // Deserialize response
        List<CatalogueItem> availableItems = response.readEntity(new GenericType<List<CatalogueItem>>(){});

        // Convert to hash map to perform Catalogue item lookups
        Map<Integer, CatalogueItem> catalogue = availableItems
                .stream()
                .collect(Collectors.toMap(CatalogueItem::getId, item -> item));

        // Calculate total amount of order
        double totalAmount = request.getItems()
                .stream()
                // Filter invalid order items
                .filter(item -> catalogue.containsKey(item.getItemId()))
                .mapToDouble(
                        orderItem -> catalogue.get(orderItem.getItemId()).getPrice() * orderItem.getCount()
                ).sum();

        // Retrieve shop name from configuration
        String shopName = conf.getShopName();

        // Prepare payment request
        PaymentRequest paymentRequest = new PaymentRequest(shopName, ccNumber, totalAmount);

        // Initiate payment
        try {
            Response paymentReponse;
            if (conf.isConnectProxyEnabled()) {
                // TODO Step 5: Use Consul-managed proxy to connect to the Payment Service
                paymentReponse = Response.status(Response.Status.SERVICE_UNAVAILABLE).entity("Payment Service not available. Implement me!").build();

            } else {
                paymentReponse = client.target("http://"+conf.getPaymentServiceIp()+":9090")
                        .path("payment")
                        .request(MediaType.APPLICATION_JSON_TYPE)
                        .post(Entity.entity(paymentRequest, MediaType.APPLICATION_JSON_TYPE));
            }

            LOGGER.info("Order placed for user {} with total amount: {}", request.getUser(), totalAmount);
            return paymentReponse;

        } catch (Exception e) {
            LOGGER.error("Unable to reach Payment Service, check error log for stack trace.");
            e.printStackTrace();
            return Response.status(Response.Status.SERVICE_UNAVAILABLE).entity("Unable to reach Payment Service. Check log for details.").build();
        }
    }

    private String fakeCreditCardLookup(String user) {
        return user.equals("Karl") ? "9999-0000-1111-2222" : "8888-0000-1111-2222";
    }
}
