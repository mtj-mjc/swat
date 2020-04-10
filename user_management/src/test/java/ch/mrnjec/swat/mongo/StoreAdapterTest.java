package ch.mrnjec.swat.mongo;

import ch.mrnjec.swat.entities.Store;
import org.bson.types.ObjectId;

/**
 * Test Class for {@link StoreAdapter}.
 *
 * @since: 24.04.2019
 * @author: Matej Mrnjec
 */
class StoreAdapterTest extends EntityAdapterExtendedTest<Store> {
    public final static ObjectId ID_TEST = new ObjectId();
    public final static String NAME_TEST = "Horw";

    @Override
    ObjectId getId() {
        return ID_TEST;
    }

    @Override
    String getName() {
        return NAME_TEST;
    }

    @Override
    Store getTestEntity() {
        return new Store(ID_TEST,NAME_TEST);
    }

    @Override
    Class<? extends EntityAdapterExtended<Store>> getAdapterClass() {
        return StoreAdapter.class;
    }
}