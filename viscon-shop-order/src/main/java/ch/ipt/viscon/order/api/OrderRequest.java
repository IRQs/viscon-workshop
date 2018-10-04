package ch.ipt.viscon.order.api;

import java.util.List;

public class OrderRequest {

    private String user;

    private List<OrderItem> items;

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public List<OrderItem> getItems() {
        return items;
    }

    public void setItems(List<OrderItem> items) {
        this.items = items;
    }
}
