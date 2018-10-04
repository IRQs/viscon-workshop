package ch.ipt.viscon.order;

import ch.ipt.viscon.order.managed.ConsulKvCache;
import ch.ipt.viscon.order.resources.InfoResource;
import ch.ipt.viscon.order.resources.OrderResource;
import com.orbitz.consul.Consul;
import com.orbitz.consul.KeyValueClient;
import com.orbitz.consul.cache.KVCache;
import io.dropwizard.Application;
import io.dropwizard.client.JerseyClientBuilder;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

import javax.ws.rs.client.Client;

public class OrderApplication extends Application<OrderConfiguration> {
    public static void main(String[] args) throws Exception {
        new OrderApplication().run(args);
    }

    @Override
    public String getName() {
        return "hello-world";
    }

    @Override
    public void initialize(Bootstrap<OrderConfiguration> bootstrap) {
        // nothing to do yet
    }

    @Override
    public void run(OrderConfiguration configuration,
                    Environment environment) {

        // Jersey HTTP Client
        final Client client = new JerseyClientBuilder(environment).using(configuration.getJerseyClientConfiguration())
                .build(getName());

        // Connect to Consul agent running on localhost
        final OrderResource orderResource;


        if (configuration.isServiceDiscoveryEnabled()) {
            Consul consulClient = Consul.builder().build();
            orderResource = new OrderResource(configuration, client, consulClient);

            // Try to retrieve shop name (if already in Consul), otherwise fallback to default
            final KeyValueClient kvClient = consulClient.keyValueClient();
            kvClient.getValueAsString(configuration.getConsulConfigurationKey() + "/shopname").ifPresent(shopName -> {
                configuration.setShopName(shopName);
            });


            // Instantiate managed Consul KV Cache that listens for config changes
            KVCache cache = KVCache.newCache(kvClient, configuration.getConsulConfigurationKey());
            ConsulKvCache managedCache = new ConsulKvCache(cache, configuration);
            environment.lifecycle().manage(managedCache);
        } else {
            orderResource = new OrderResource(configuration, client);
        }

        final InfoResource infoResource = new InfoResource(configuration);

        environment.jersey().register(infoResource);
        environment.jersey().register(orderResource);
    }

}
