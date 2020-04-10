package ch.mrnjec.swat.mongo;

import ch.mrnjec.swat.entities.Category;
import ch.mrnjec.swat.entities.Product;
import ch.mrnjec.swat.entities.Sortiment;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import org.bson.types.ObjectId;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Initialize User Management Database
 *
 * @since: 14.05.2019
 * @author: Matej Mrnjec
 */
public class MongoDBInit {

    public static void main(final String[] args) throws IOException {
        deleteDatabase(MongoDbConfig.DATABASE);
        createSortiment();
        createCategory();
        createProducts();
    }

    private static void deleteDatabase(String name){
        MongoDbConfig config = new MongoDbConfig();
        MongoClient mongoClient = new MongoClient(new MongoClientURI(config.getConnectionAddress()));
        mongoClient.getDatabase(name).drop();
    }

    public static void createProducts() throws IOException {
        AdapterFactory factory = new AdapterFactory();
        SortimentAdapter sortimentAdapter = (SortimentAdapter) factory.getAdapter(Sortiment.class);
        ProductAdapter productAdapter = (ProductAdapter) factory.getAdapter(Product.class);
        CategoryAdapter categoryAdapter = (CategoryAdapter) factory.getAdapter(Category.class);
        List<Product> products = new ArrayList<>();
        products.add(new Product(new ObjectId(),
                "Staubsauger",
                "Eine Machine die Staub saugt",
                199.99,
                sortimentAdapter.getByName("Horw").getId().toString(),
                categoryAdapter.getByName("Haushalt").getId().toString(),
                5));
        products.add(new Product(new ObjectId(),
                "iPhone 6",
                "Apple Smartphone der 6.Generation",
                549.99,
                sortimentAdapter.getByName("Ebikon").getId().toString(),
                categoryAdapter.getByName("Smartphone").getId().toString(),
                6));
        products.add(new Product(new ObjectId(),
                "Salat",
                "Grün Zeug",
                5.00,
                sortimentAdapter.getByName("Luzern Bahnhof").getId().toString(),
                categoryAdapter.getByName("Essen").getId().toString(),
                3));
        products.forEach(product -> {
            try {
                productAdapter.create(product);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    public static void createSortiment() throws IOException {
        AdapterFactory factory = new AdapterFactory();
        SortimentAdapter adapter = (SortimentAdapter) factory.getAdapter(Sortiment.class);
        List<Sortiment> groups = new ArrayList<>();
        groups.add(new Sortiment(new ObjectId(), "Horw"));
        groups.add(new Sortiment(new ObjectId(), "Ebikon"));
        groups.add(new Sortiment(new ObjectId(), "Luzern Bahnhof"));
        groups.add(new Sortiment(new ObjectId(), "Rotkreuz"));
        groups.forEach(sortiment -> {
            try {
                adapter.create(sortiment);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    public static void createCategory() throws IOException {
        AdapterFactory factory = new AdapterFactory();
        CategoryAdapter adapter = (CategoryAdapter) factory.getAdapter(Category.class);
        List<Category> stores = new ArrayList<>();
        stores.add(new Category(new ObjectId(), "Smartphone"));
        stores.add(new Category(new ObjectId(), "Essen"));
        stores.add(new Category(new ObjectId(), "Haushalt"));
        stores.add(new Category(new ObjectId(), "Spielzeug"));
        stores.forEach(category -> {
            try {
                adapter.create(category);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }
}
