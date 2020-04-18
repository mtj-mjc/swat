package ch.mrnjec.swat.mongo;

import ch.mrnjec.swat.entities.Sortiment;
import org.bson.types.ObjectId;

import java.util.Arrays;
import java.util.List;

/**
 * Test Class for {@link SortimentAdapter} .
 *
 * @since: 23.05.2019
 * @author: matej
 */
class SortimentAdapterTest extends EntityAdapterExtendedTest<Sortiment> {
    public static final String TEST_STR = "Test";
    public static final String NOID = "000000000000000000000000";
    public static final ObjectId TEST_ID = new ObjectId(NOID);

    @Override
    ObjectId getId() {
        return TEST_ID;
    }

    @Override
    String getName() {
        return TEST_STR;
    }

    @Override
    Sortiment getTestEntity() {
        return new Sortiment(TEST_ID, TEST_STR);
    }

    @Override
    List<Sortiment> getTestEntities() {
        return Arrays.asList(new Sortiment(TEST_ID, TEST_STR)
                            ,new Sortiment(TEST_ID, TEST_STR));
    }

    @Override
    Class<? extends EntityAdapterExtended<Sortiment>> getAdapterClass() {
        return SortimentAdapter.class;
    }
}