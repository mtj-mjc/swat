package ch.mrnjec.swat.micro;

import ch.mrnjec.swat.entities.Store;
import ch.mrnjec.swat.mongo.AdapterFactory;
import ch.mrnjec.swat.mongo.StoreAdapter;

import java.io.IOException;
import java.util.List;

public class StoreService {
    private final AdapterFactory adapterFactory;

    public StoreService() {
        this.adapterFactory = new AdapterFactory();
    }

    public List<Store> getAllStores() throws IOException {
        StoreAdapter adapter = (StoreAdapter) this.adapterFactory.getAdapter(Store.class);
        return adapter.getAll();
    }
}
