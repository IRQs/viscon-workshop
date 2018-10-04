package ch.ipt.viscon.order;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.dropwizard.Configuration;
import io.dropwizard.client.JerseyClientConfiguration;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

public class OrderConfiguration extends Configuration {

    @NotEmpty
    private String shopName = "Generic 0815 Shop";

    @JsonProperty
    public String getShopName() {
        return shopName;
    }

    @JsonProperty
    public void setShopName(String name) {
        this.shopName = name;
    }

    @NotEmpty
    private String catalogueServiceIp;
    @NotEmpty
    private String paymentServiceIp;

    private boolean serviceDiscoveryEnabled = false;

    @JsonProperty
    public boolean isServiceDiscoveryEnabled() {
        return serviceDiscoveryEnabled;
    }

    @JsonProperty
    public void setServiceDiscoveryEnabled(boolean serviceDiscoveryEnabled) {
        this.serviceDiscoveryEnabled = serviceDiscoveryEnabled;
    }

    private boolean connectProxyEnabled = false;

    public boolean isConnectProxyEnabled() {
        return connectProxyEnabled;
    }

    public void setConnectProxyEnabled(boolean connectProxyEnabled) {
        this.connectProxyEnabled = connectProxyEnabled;
    }

    @NotEmpty
    private String consulConfigurationKey;

    public String getConsulConfigurationKey() {
        return consulConfigurationKey;
    }

    public void setConsulConfigurationKey(String consulConfigurationKey) {
        this.consulConfigurationKey = consulConfigurationKey;
    }

    @JsonProperty
    public String getCatalogueServiceIp() {
        return catalogueServiceIp;
    }

    @JsonProperty
    public void setCatalogueServiceIp(String catalogueServiceIp) {
        this.catalogueServiceIp = catalogueServiceIp;
    }

    @JsonProperty
    public String getPaymentServiceIp() {
        return paymentServiceIp;
    }

    @JsonProperty
    public void setPaymentServiceIp(String paymentServiceIp) {
        this.paymentServiceIp = paymentServiceIp;
    }

    @Valid
    @NotNull
    private JerseyClientConfiguration jerseyClient = new JerseyClientConfiguration();

    @JsonProperty("jerseyClient")
    public JerseyClientConfiguration getJerseyClientConfiguration() {
        return jerseyClient;
    }

    @JsonProperty("jerseyClient")
    public void setJerseyClientConfiguration(JerseyClientConfiguration jerseyClient) {
        this.jerseyClient = jerseyClient;
    }
}
