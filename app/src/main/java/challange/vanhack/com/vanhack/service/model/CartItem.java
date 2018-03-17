package challange.vanhack.com.vanhack.service.model;

import java.util.UUID;

public class CartItem {

    String id;
    Product product;
    int quantity;



    public CartItem(Product p, int q){
        id = UUID.randomUUID().toString();
        product = p;
        quantity = q;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
