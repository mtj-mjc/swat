package ch.mrnjec.swat.micro;

import ch.mrnjec.swat.entities.OrderPosition;
import ch.mrnjec.swat.entities.Product;
import ch.mrnjec.swat.mongo.AdapterFactory;
import ch.mrnjec.swat.mongo.ProductAdapter;

import java.io.IOException;
import java.util.List;

public class ProductService {
    private final AdapterFactory adapterFactory;
    public ProductService() {
        this.adapterFactory = new AdapterFactory();
    }

    public List<Product> getAllProducts() throws IOException {
        ProductAdapter adapter = (ProductAdapter) this.adapterFactory.getAdapter(Product.class);
        return adapter.getAll();
    }

    public void bookProducts(List<OrderPosition> positions) throws IOException {
        for(OrderPosition position : positions) {
            this.bookProduct(position.getProduct(), position.getCount());
        }
    }

    public void bookProduct(Product product, int quantity) throws IOException {
        ProductAdapter adapter = (ProductAdapter) this.adapterFactory.getAdapter(Product.class);
        adapter.bookProduct(product, quantity);
    }
}
