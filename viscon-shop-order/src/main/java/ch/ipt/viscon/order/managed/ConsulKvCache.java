package ch.ipt.viscon.order.managed;

import ch.ipt.viscon.order.OrderConfiguration;
import com.orbitz.consul.cache.KVCache;
import com.orbitz.consul.model.kv.Value;
import io.dropwizard.lifecycle.Managed;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

public class ConsulKvCache implements Managed {

    private static final Logger LOGGER = LoggerFactory.getLogger(ConsulKvCache.class);

    private final KVCache cache;
    private final OrderConfiguration conf;

    public ConsulKvCache(KVCache cache, OrderConfiguration conf) {
        this.cache = cache;
        this.conf = conf;
    }

    @Override
    public void start() throws Exception {
        // TODO Step: Configure your cache to retrieve your shop name dynamically
        // Don't forget to set the retrieved shop name on the OrderConfiguration instance
        // Refer to Example 5 @ https://github.com/rickfast/consul-client for implementation hints

        String shopNameKey = conf.getConsulConfigurationKey() + "/shopname";

        cache.addListener(newValues -> {
           if (newValues.containsKey(shopNameKey)) {

                Optional<String> newValue = newValues.get(shopNameKey).getValueAsString();

                newValue.ifPresent(shopname -> {
                    LOGGER.info("New shop name retrieved from Consul: {}", shopname);
                    conf.setShopName(shopname);
                });

           }
        });

        cache.start();
    }

    @Override
    public void stop() throws Exception {
        cache.stop();
    }
}
